package com.android.gevart.awesomechat.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.android.gevart.awesomechat.R;
import com.android.gevart.awesomechat.models.User;
import com.android.gevart.awesomechat.adapters.UsersAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UsersListActivity extends AppCompatActivity {

    private DatabaseReference usersDatabaseReference;
    private ChildEventListener childEventListener;
    private FirebaseAuth auth;
    private ArrayList<User> usersArrayList;
    private RecyclerView usersRecyclerView;
    private UsersAdapter usersAdapter;
    private RecyclerView.LayoutManager usersLayoutManager;
    private String senderUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);

        Intent intent = getIntent();
        if (intent != null) {
            senderUserName = intent.getStringExtra("senderUserName");
        }

        auth = FirebaseAuth.getInstance();
        buildRecyclerView();
        attachUsersDatabaseReferenceListener();
    }

    private void buildRecyclerView() {
        usersArrayList = new ArrayList<>();
        usersRecyclerView = findViewById(R.id.usersListRecyclerView);
        usersRecyclerView.setHasFixedSize(true);
        usersRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        usersLayoutManager = new LinearLayoutManager(this);
        usersRecyclerView.setLayoutManager(usersLayoutManager);
        usersAdapter = new UsersAdapter(usersArrayList);
        usersRecyclerView.setAdapter(usersAdapter);

        usersAdapter.setOnUserClickListener(new UsersAdapter.OnUserClickListener() {
            @Override
            public void onUserClick(int position) {
                goToChat(position);
            }
        });
    }

    private void goToChat(int position) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("recipientUserId", usersArrayList.get(position).getId());
        intent.putExtra("recipientUserName", usersArrayList.get(position).getName());
        intent.putExtra("senderUserName", senderUserName);
        startActivity(intent);
    }

    private void attachUsersDatabaseReferenceListener() {
        usersDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        if (childEventListener == null) {
            childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    User user = dataSnapshot.getValue(User.class);
                    if (!auth.getCurrentUser().getUid().equals(user.getId())) {
                        user.setAvatarMockUpResource(R.drawable.user_image);
                        usersArrayList.add(user);
                        usersAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            };

            usersDatabaseReference.addChildEventListener(childEventListener);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out:
                auth.signOut();
                startActivity(new Intent(this, SignInActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
