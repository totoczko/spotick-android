package com.example.spotick;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

public class UserPostsAdapter extends BaseAdapter {
    private Context mContext;
    private List<Post> userPostsList;

    // Constructor
    public UserPostsAdapter(Context mContext, List<Post> userPostsList){
        this.mContext = mContext;
        this.userPostsList = userPostsList;
    }

    @Override
    public int getCount() {
        return userPostsList.size();
    }

    @Override
    public Object getItem(int position) {
        return userPostsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(mContext);
        Glide.with(mContext)
                .load(userPostsList.get(position).getImg())
                .into(imageView);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(550, 550));
        return imageView;
    }

}