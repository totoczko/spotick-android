package com.example.spotick;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.myViewHolder> {

    Context mContext;
    List<Post> mData;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

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

        return new myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, final int position) {

        final Post singlePost = mData.get(position);

        Glide.with(mContext)
                .load(mData.get(position).getImg())
                .into(holder.postImage);
        holder.shortText.setText(mData.get(position).getShortText());
        holder.likesCounter.setText(String.valueOf(mData.get(position).getLikesCount()));
        holder.location.setText(mData.get(position).getGeo());
        holder.date.setText(mData.get(position).getDataString());
        holder.userName.setText(mData.get(position).getUserName());
        holder.avatar.setText(String.valueOf(mData.get(position).getUserFirstLetter()));
        String color = mData.get(position).getUserColor();
        Drawable drawable = mContext.getResources().getDrawable(R.drawable.avatar);
        drawable.setColorFilter(Color.parseColor(color), PorterDuff.Mode.SRC_ATOP);
        holder.avatar.setBackgroundDrawable(drawable);

        holder.like.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Map<String, Object> likes = new HashMap<>();
                likes = singlePost.getLikes();
                Toast.makeText(view.getContext(), String.valueOf(likes), Toast.LENGTH_LONG).show();

                Map<String, Object> likesCount = mData.get(position).getLikes();
                ArrayList<String> likedBy = (ArrayList<String>) likesCount.get("users");

                if (likedBy != null) {
                    for (String user : likedBy) {
                        Boolean isLikedByUser = user.equals(currentUser.getUid());
                        if (!isLikedByUser) {
                            Toast.makeText(view.getContext(), "not liked!", Toast.LENGTH_LONG).show();
                            view.getBackground().setColorFilter(view.getResources().getColor(R.color.red), PorterDuff.Mode.MULTIPLY);
                        }else{
                            Toast.makeText(view.getContext(), "liked!", Toast.LENGTH_LONG).show();
                            view.getBackground().setColorFilter(view.getResources().getColor(R.color.colorLightGray), PorterDuff.Mode.MULTIPLY);
                        }
                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder{

        ImageView postImage;
        TextView shortText, likesCounter, location, date, userName, avatar;
        ImageButton like;


        public myViewHolder(View itemView){
            super(itemView);
            postImage = itemView.findViewById(R.id.post_image);
            shortText = itemView.findViewById(R.id.short_text);
            likesCounter = itemView.findViewById(R.id.likes_counter);
            location = itemView.findViewById(R.id.location);
            date = itemView.findViewById(R.id.date);
            userName = itemView.findViewById(R.id.username);
            avatar = itemView.findViewById(R.id.avatar);
            like = itemView.findViewById(R.id.likes_button);

        }
    }
}
