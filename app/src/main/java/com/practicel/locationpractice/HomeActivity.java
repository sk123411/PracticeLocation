package com.practicel.locationpractice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import java.util.List;

public class HomeActivity extends AppCompatActivity implements FirebaseLoadingListener {
    private FirebaseRecyclerAdapter<User, UserViewHolder> adapter,searchAdapter;
    private MaterialSearchBar searchBar;
    private RecyclerView recyclerView;
    FirebaseLoadingListener firebaseLoadingListener;

    List<String> suggestList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
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
               // Toast.makeText(getContext(),"search Confirmed",Toast.LENGTH_SHORT).show();
                startSearchQuery(text.toString());
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });
        recyclerView = findViewById(R.id.recyclerFriendList);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);

        recyclerView.addItemDecoration(new DividerItemDecoration(this,manager.getOrientation()));

        loadFriendList();
        //loadSearchData();
    }

    private void loadSearchData() {
        final List<String> searchString = new ArrayList<>();
        DatabaseReference userList = FirebaseDatabase.getInstance().getReference(Common.USERS)
                .child(Common.loggedUser.getUid()).child(Common.ACCEPLIST);
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

    private void loadFriendList() {


        Query query = FirebaseDatabase.getInstance().getReference(Common.USERS)
                .child(Common.loggedUser.getUid()).child(Common.ACCEPLIST);

        FirebaseRecyclerOptions<User> options = new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(query,User.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<User, UserViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull final User model) {
                holder.textUsername.setText(new StringBuilder(model.getEmail()));


                holder.setiRecylerItemClickListerner(new IRecylerItemClickListerner() {
                    @Override
                    public void onClick(View v, int pos) {

                          Common.trackingUser = model;
                           startActivity(new Intent(v.getContext(), TrackingActivity.class));
                    }
                });
            }

            @NonNull
            @Override
            public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item,parent,false);
                return new UserViewHolder(v);            }
        };

        adapter.startListening();
        recyclerView.setAdapter(adapter);



    }


    @Override
    public void onResume() {
        super.onResume();

        if (adapter!=null)
            adapter.startListening();
        if (searchAdapter!=null)
            searchAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();

        if (adapter!=null)
            adapter.stopListening();
        if (searchAdapter!=null)
            searchAdapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (adapter!=null)
            adapter.startListening();
        if (searchAdapter!=null)
            searchAdapter.startListening();

    }



    private void startSearchQuery(String toString) {


        Query query = FirebaseDatabase.getInstance().getReference(Common.USERS)
                .child(Common.loggedUser.getUid()).child(Common.ACCEPLIST).orderByChild("name").startAt(toString);
        FirebaseRecyclerOptions<User> options = new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(query,User.class)
                .build();

        searchAdapter = new FirebaseRecyclerAdapter<User, UserViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull final User model) {
                holder.textUsername.setText(new StringBuilder(model.getEmail()));


                holder.setiRecylerItemClickListerner(new IRecylerItemClickListerner() {
                    @Override
                    public void onClick(View v, int pos) {

                        //  Common.trackingUser = model;
                        //   startActivity(new Intent(v.getContext(), TrackingUserActivity.class));
                    }
                });
            }

            @NonNull
            @Override
            public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item,parent,false);
                return new UserViewHolder(v);            }
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
