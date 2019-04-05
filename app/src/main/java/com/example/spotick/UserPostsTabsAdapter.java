package com.example.spotick;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class UserPostsTabsAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public UserPostsTabsAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                UserPosts tab1 = new UserPosts();
                return tab1;
            case 1:
                LikedPosts tab2 = new LikedPosts();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}