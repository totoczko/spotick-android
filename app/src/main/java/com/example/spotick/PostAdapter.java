package com.example.spotick;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.myViewHolder> {

    Context mContext;
    List<Post> mData;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference postRef;
    private FirebaseDatabase database;


    public PostAdapter(Context mContext, List<Post> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.post_card, parent, false);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();

        return new myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, final int position) {
        holder.setIsRecyclable(false);
        Integer currentPosition =getItemViewType(position);
        final Post singlePost = mData.get(currentPosition);

        Glide.with(mContext)
                .load(singlePost.getImg())
                .into(holder.postImage);
        holder.shortText.setText(singlePost.getShortText());
        holder.likesButton.setText(String.valueOf(singlePost.getLikesCount()));
        holder.location.setText(singlePost.getGeo());
        holder.date.setText(singlePost.getDataString());
        holder.userName.setText(singlePost.getUserName());
        holder.avatar.setText(String.valueOf(singlePost.getUserFirstLetter()));
        String color = singlePost.getUserColor();
        Drawable drawableAvatar = mContext.getResources().getDrawable(R.drawable.avatar);
        drawableAvatar.setColorFilter(Color.parseColor(color), PorterDuff.Mode.SRC_ATOP);
        holder.avatar.setBackgroundDrawable(drawableAvatar);

        ArrayList<String> likedBy;
        likedBy = singlePost.getLikesUsers();

        final Boolean[] isLiked = {false, false};
        if (likedBy != null) {
            for (String user : likedBy) {
                if(currentUser != null){
                    if(user.equals(currentUser.getUid())){
                        isLiked[0] = true;
                        isLiked[1] = true;
                        Drawable drawableIcon = mContext.getResources().getDrawable(R.drawable.ic_favorite).mutate();
                        drawableIcon = DrawableCompat.wrap(drawableIcon);
                        DrawableCompat.setTint(drawableIcon, Color.RED);
                        DrawableCompat.setTintMode(drawableIcon, PorterDuff.Mode.SRC_ATOP);
                        holder.likesButton.setCompoundDrawablesWithIntrinsicBounds(drawableIcon, null, null, null);
                    }
                }
            }
        }


        holder.likesButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                Button likesButton = view.findViewById(R.id.likes_button);
                Long count = singlePost.getLikesCount();
                postRef = database.getReference().child("posts").child(singlePost.id).child("likes");

                Drawable drawableIcon = mContext.getResources().getDrawable(R.drawable.ic_favorite).mutate();
                drawableIcon = DrawableCompat.wrap(drawableIcon);

                ArrayList<String> users;
                users = singlePost.getLikesUsers();

                int next = 0;

                if(users != null){
                    next = users.size();
                }

                if (isLiked[0] && isLiked[1]){
                    isLiked[0] = false;
                    postRef.child("count").setValue(count - 1);
//                    DrawableCompat.setTint(drawableIcon, Color.LTGRAY);
//                    likesButton.setText(String.valueOf(count - 1));
                    if (users != null) {
                        int i = 0;
                        for (String user : users) {
                            if(user.equals(currentUser.getUid())){
                                users.remove(i);
                                return;
                            }
                            i++;
                        }
                    }

                    postRef.child("users").setValue(users);

                }else if (isLiked[0] && !isLiked[1]){

                    isLiked[0] = false;
                    postRef.child("count").setValue(count);
//                    DrawableCompat.setTint(drawableIcon, Color.LTGRAY);
//                    likesButton.setText(String.valueOf(count));

                    if (users != null) {
                        int i = 0;
                        for (String user : users) {
                            if(user.equals(currentUser.getUid())){
                                users.remove(i);
                                return;
                            }
                            i++;
                        }
                    }

                    postRef.child("users").setValue(users);

                }else if(!isLiked[0] && !isLiked[1]){

                    isLiked[0] = true;
                    postRef.child("count").setValue(count + 1);
                    DatabaseReference newUserRef = postRef.child("users/" + next);
                    newUserRef.setValue(currentUser.getUid());
//                    DrawableCompat.setTint(drawableIcon, Color.RED);
//                    likesButton.setText(String.valueOf(count + 1));

                }else if(!isLiked[0] && isLiked[1]){

                    isLiked[0] = true;
                    DatabaseReference newUserRef = postRef.child("users/" + next);
                    newUserRef.setValue(currentUser.getUid());
//                    DrawableCompat.setTint(drawableIcon, Color.RED);
//                    likesButton.setText(String.valueOf(count));

                }

                DrawableCompat.setTintMode(drawableIcon, PorterDuff.Mode.SRC_ATOP);
                likesButton.setCompoundDrawablesWithIntrinsicBounds(drawableIcon, null, null, null);

            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class myViewHolder extends RecyclerView.ViewHolder{

        ImageView postImage;
        TextView shortText, location, date, userName, avatar;
        Button likesButton;


        public myViewHolder(View itemView){
            super(itemView);
            postImage = itemView.findViewById(R.id.post_image);
            shortText = itemView.findViewById(R.id.short_text);
            likesButton = itemView.findViewById(R.id.likes_button);
            location = itemView.findViewById(R.id.location);
            date = itemView.findViewById(R.id.date);
            userName = itemView.findViewById(R.id.username);
            avatar = itemView.findViewById(R.id.avatar);
        }
    }
}
