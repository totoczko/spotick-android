package com.example.spotick;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

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
import java.util.Map;

public class UserLikes extends Fragment {

    private FirebaseDatabase database;
    private DatabaseReference databaseRef;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.user_liked_posts, container, false);
        GridView gridView = (GridView) view.findViewById(R.id.grid_view_liked_posts);

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();

        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference("posts");

        final List userLikes = new ArrayList();
        final UserPostsAdapter adapter =new UserPostsAdapter(view.getContext(), userLikes);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getContext(), "Clicked " + userLikes.get(i), Toast.LENGTH_SHORT).show();
            }
        });

        databaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Post singlePost = dataSnapshot.getValue(Post.class);

                Map<String, Object> likesCount = singlePost.getLikes();
                ArrayList<String> likedBy = (ArrayList<String>) likesCount.get("users");

                if(likedBy != null){
                    for (String user : likedBy) {
                        Boolean isLikedByUser = user.equals(currentUser.getUid());
                        if(isLikedByUser){
                            userLikes.add(new Post(
                                    singlePost.id,
                                    singlePost.shortText,
                                    singlePost.geo, (long)
                                    singlePost.data,
                                    singlePost.imageid,
                                    singlePost.img, (Long)
                                    singlePost.likes.get("count"),
                                    (String) singlePost.user.get("name"),
                                    (String) singlePost.user.get("color"),
                                    (String) singlePost.user.get("id")
                            ));

                            Collections.sort(userLikes, new Comparator<Post>(){
                                public int compare(Post obj1, Post obj2) {
                                    return Long.valueOf(obj2.data).compareTo(Long.valueOf(obj1.data));
                                }
                            });
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
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

        return view;
    }

}
