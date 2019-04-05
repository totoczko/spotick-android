package com.example.spotick;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

public class UserPosts extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.user_posts, container, false);
        GridView gridView = (GridView) view.findViewById(R.id.grid_view_user_posts);

        gridView.setAdapter(new UserPostsAdapter(view.getContext()));
        return view;
    }
}