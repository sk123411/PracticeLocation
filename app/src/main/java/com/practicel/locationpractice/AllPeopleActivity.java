package com.practicel.locationpractice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class AllPeopleActivity extends AppCompatActivity implements FirebaseLoadingListener {
    private FirebaseRecyclerAdapter<User, UserViewHolder> adapter,searchAdapter;
    private MaterialSearchBar searchBar;

    List<String> suggestList = new ArrayList<>();
    FirebaseLoadingListener firebaseLoadingListener;
    private RecyclerView recyclerView;
    CompositeDisposable disposable = new CompositeDisposable();
    IFCMService ifcmService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_people);
        ifcmService = Common.getFCMService();
        searchBar = findViewById(R.id.searchBar);
        searchBar.setCardViewElevation(10);


        searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                List<String> suggest = new ArrayList<>();

                for (String search:suggestList){

                    if (search.toLowerCase().contains(searchBar.getText().toLowerCase()))
                        suggest.add(search);




                }

                searchBar.setLastSuggestions(suggest);


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {

                Toast.makeText(getApplicationContext(),"search status changed",Toast.LENGTH_SHORT).show();

                if (!enabled){

                    if (adapter!=null){

                        recyclerView.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                Toast.makeText(getApplicationContext(),"search Confirmed",Toast.LENGTH_SHORT).show();
                startSearchQuery(text.toString());
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });
        recyclerView = findViewById(R.id.recyclerUser);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);

        recyclerView.addItemDecoration(new DividerItemDecoration(this,manager.getOrientation()));
        firebaseLoadingListener= this;


        loadUserList();
        loadSearchData();
    }
    private void loadSearchData() {

        final List<String> searchString = new ArrayList<>();
        DatabaseReference userList = FirebaseDatabase.getInstance().getReference(Common.USERS);
        userList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot:dataSnapshot.getChildren()){

                    User user = snapshot.getValue(User.class);

                    searchString.add(user.getEmail());

                }
                // suggestList.add

                firebaseLoadingListener.onFirebaseUserSearchDone(searchString);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                firebaseLoadingListener.onFirebaseLoadFailed(databaseError.getMessage());
            }
        });
    }

    private void loadUserList() {


        Query query = FirebaseDatabase.getInstance().getReference(Common.USERS);

        FirebaseRecyclerOptions<User> options = new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(query,User.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<User, UserViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull final User model) {

                if (model.getEmail().equals(Common.loggedUser.getEmail())){

                    holder.textUsername.setText(new StringBuilder(model.getEmail()).append(" me"));
                }else {

                    holder.textUsername.setText(new StringBuilder(model.getEmail()));
                }

                holder.setiRecylerItemClickListerner(new IRecylerItemClickListerner() {
                    @Override
                    public void onClick(View v, int pos) {
                         showDialogRequest(model);
                    }
                });


            }

            @NonNull
            @Override
            public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item,parent,false);
                return new UserViewHolder(v);
            }
        };

        adapter.startListening();
        recyclerView.setAdapter(adapter);




    }


    private void showDialogRequest(final User model) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.RequestDialog);
        builder.setTitle("request friend")
                .setMessage("Do you want to send request" + model.getEmail());
        builder.setIcon(R.drawable.ic_person_black_24dp)
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                DatabaseReference acceptList = FirebaseDatabase.getInstance().getReference(Common.USERS)
                        .child(Common.loggedUser.getUid()).child(Common.ACCEPLIST);

                acceptList.orderByKey().equalTo(model.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.getValue()==null){

                            sendFriendRequest(model);
                        }else {

                            Toast.makeText(getApplicationContext(),"you and " + model.getEmail() +"already frined",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
        builder.show();
    }

    private void sendFriendRequest(final User model) {

        DatabaseReference tokes = FirebaseDatabase.getInstance().getReference("tokens");
        tokes.orderByKey().equalTo(model.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()==null){

                    Toast.makeText(getApplicationContext(),"token error",Toast.LENGTH_SHORT).show();

                }else {

                    final MyRequest myRequest = new MyRequest();

                    Map<String,String> data = new HashMap<>();
                    data.put(Common.FROM_UID,Common.loggedUser.getUid());
                    data.put(Common.FROM_NAME,Common.loggedUser.getEmail());
                    data.put(Common.TO_UID,model.getUid());
                    data.put(Common.TO_NAME,model.getEmail());
                    myRequest.setTo(dataSnapshot.child(model.getUid()).getValue(String.class));
                    myRequest.setData(data);

                    disposable.add(ifcmService.sendFriendRequestToUser(myRequest).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<MyResponse>() {
                        @Override
                        public void accept(MyResponse myResponse) throws Exception {
                            if (myResponse.success==1)
                                Toast.makeText(getApplicationContext(),"request sent",Toast.LENGTH_SHORT).show();

                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {

                            Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (adapter!=null) {
            adapter.startListening();
        }
        if (searchAdapter!=null) {
            searchAdapter.startListening();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (adapter!=null) {
            adapter.startListening();
        }
        if (searchAdapter!=null) {
            searchAdapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (adapter!=null) {
            adapter.stopListening();
        }
        if (searchAdapter!=null) {
            searchAdapter.stopListening();
        }
       // disposable.clear();
    }

    private void startSearchQuery(String toString) {

        Query query = FirebaseDatabase.getInstance().getReference(Common.USERS)
                .orderByChild("name").startAt(toString);
        FirebaseRecyclerOptions<User> options = new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(query,User.class)
                .build();

        searchAdapter = new FirebaseRecyclerAdapter<User, UserViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull User model) {

                if (model.getEmail().equals(Common.loggedUser.getEmail())){

                    holder.textUsername.setText(new StringBuilder(model.getEmail()).append(" me"));
                }else {

                    holder.textUsername.setText(new StringBuilder(model.getEmail()));
                }

                holder.setiRecylerItemClickListerner(new IRecylerItemClickListerner() {
                    @Override
                    public void onClick(View v, int pos) {

                    }
                });


            }

            @NonNull
            @Override
            public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item,parent,false);
                return new UserViewHolder(v);
            }
        };

        searchAdapter.startListening();
        recyclerView.setAdapter(searchAdapter);


    }

    @Override
    public void onFirebaseUserSearchDone(List<String> users) {
        searchBar.setLastSuggestions(users);
    }

    @Override
    public void onFirebaseLoadFailed(String message) {

    }


}
