package com.practicel.locationpractice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.List;

public class FriendRequestActivity extends AppCompatActivity implements FirebaseLoadingListener  {
    private FirebaseRecyclerAdapter<User, FriendRequestViewHolder> adapter,searchAdapter;
    private MaterialSearchBar searchBar;

    List<String> suggestList = new ArrayList<>();
    FirebaseLoadingListener firebaseLoadingListener;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_request);


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


        loadRequestList();
        loadSearchData();



    }

    private void startSearchQuery(String toString) {



        Query query = FirebaseDatabase.getInstance().getReference(Common.USERS)
                .child(Common.loggedUser.getUid()).child(Common.FRIEND_REQUEST).orderByChild("name").startAt(toString);
        FirebaseRecyclerOptions<User> options = new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(query,User.class)
                .build();

        searchAdapter = new FirebaseRecyclerAdapter<User, FriendRequestViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FriendRequestViewHolder holder, int position, @NonNull User model) {
                holder.textUsername.setText(new StringBuilder(model.getEmail()));

            }

            @NonNull
            @Override
            public FriendRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_request_user,parent,false);
                return new FriendRequestViewHolder(v);            }
        };

        searchAdapter.startListening();
        recyclerView.setAdapter(searchAdapter);






    }

    private void loadSearchData() {
        final List<String> searchString = new ArrayList<>();
        DatabaseReference userList = FirebaseDatabase.getInstance().getReference(Common.USERS)
                .child(Common.loggedUser.getUid()).child(Common.FRIEND_REQUEST);
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

    private void loadRequestList() {
        Query query = FirebaseDatabase.getInstance().getReference(Common.USERS).child(Common.loggedUser.getUid())
                .child(Common.FRIEND_REQUEST);

        FirebaseRecyclerOptions<User> options = new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(query,User.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<User, FriendRequestViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FriendRequestViewHolder holder, int position, @NonNull final User model) {

                holder.textUsername.setText(model.getEmail());

                holder.accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        deleteFriendRequest(model,false);
                        addToAcceptList(model);
                        addUserToFriendContact(model);
                    }
                });

                holder.decline.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteFriendRequest(model,true);

                    }
                });
            }

            @NonNull
            @Override
            public FriendRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_request_user,parent,false);
                return new FriendRequestViewHolder(v);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    private void addUserToFriendContact(User model) {

        DatabaseReference acceptRed = FirebaseDatabase.getInstance().getReference(Common.USERS).
                child(model.getUid())
                .child(Common.ACCEPLIST);

        acceptRed.child(model.getUid()).setValue(Common.loggedUser);
    }

    private void addToAcceptList(User model) {

        DatabaseReference acceptRed = FirebaseDatabase.getInstance().getReference(Common.USERS).
                child(Common.loggedUser.getUid())
                .child(Common.ACCEPLIST);

        acceptRed.child(model.getUid()).setValue(model);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (adapter!=null)
            adapter.startListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter!=null)
            adapter.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter!=null)
            adapter.stopListening();

    }

    private void deleteFriendRequest(User model, final boolean isShowMessage) {
        DatabaseReference requestRef = FirebaseDatabase.getInstance().getReference(Common.USERS).
                child(Common.loggedUser.getUid())
                .child(Common.FRIEND_REQUEST);

        requestRef.child(model.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if (isShowMessage)
                    Toast.makeText(getApplicationContext(),"remove",Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public void onFirebaseUserSearchDone(List<String> users) {
        searchBar.setLastSuggestions(users);
    }

    @Override
    public void onFirebaseLoadFailed(String message) {

    }
}
