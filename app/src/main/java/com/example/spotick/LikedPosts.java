package com.example.spotick;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

public class LikedPosts extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.liked_posts, container, false);
        GridView gridView = (GridView) view.findViewById(R.id.grid_view_liked_posts);

        gridView.setAdapter(new UserLikesAdapter(view.getContext()));
        return view;
    }
}