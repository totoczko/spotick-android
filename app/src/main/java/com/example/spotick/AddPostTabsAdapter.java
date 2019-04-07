package com.example.spotick;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

public class AddPostTabsAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public AddPostTabsAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        Log.d("CLICK 2", "getItem: " + position);
        switch (position) {
            case 0:
                AddCamera tab1 = new AddCamera();
                return tab1;
            case 1:
                AddFile tab2 = new AddFile();
                return tab2;
            case 2:
                AddInfo tab3 = new AddInfo();
                return tab3;
            case 3:
                AddInfo tab4 = new AddInfo();
                return tab4;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}