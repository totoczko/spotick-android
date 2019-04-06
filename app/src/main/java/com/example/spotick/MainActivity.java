package com.example.spotick;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference databaseRef;

    private FirebaseAuth mAuth;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_dashboard:
                    Intent intent_add = new Intent(MainActivity.this, AddActivity.class);
                    startActivity(intent_add);
                    return true;
                case R.id.navigation_notifications:
                    Intent intent_user = new Intent(MainActivity.this, UserActivity.class);
                    startActivity(intent_user);
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

//        mAuth.signOut();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null){
            String name = currentUser.getDisplayName();
            String email = currentUser.getEmail();
            Log.d("CURRENTUSER", "onCreate: " + name + " " + email);
        }

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference("posts");


        RecyclerView recyclerView = findViewById(R.id.post_list);
        final List PostList = new ArrayList();
        final PostAdapter adapter = new PostAdapter(this,PostList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        databaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Post singlePost = dataSnapshot.getValue(Post.class);
                PostList.add(new Post(
                        singlePost.id,
                        singlePost.shortText,
                        singlePost.geo, (long)
                        singlePost.data,
                        singlePost.imageid,
                        singlePost.img, (Long)
                        singlePost.likes.get("count"),
                        (String) singlePost.user.get("name"),
                        (String) singlePost.user.get("color")
                ));

                Collections.sort(PostList, new Comparator<Post>(){
                    public int compare(Post obj1, Post obj2) {
                         return Long.valueOf(obj2.data).compareTo(Long.valueOf(obj1.data));
                    }
                });

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });
//
    }

}
