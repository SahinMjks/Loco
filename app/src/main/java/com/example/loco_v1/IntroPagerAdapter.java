package com.example.loco_v1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class IntroPagerAdapter extends FragmentPagerAdapter {

    private static final int NUM_PAGES = 3;

    public IntroPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return IntroScreenPageFragment.newInstance(R.drawable.ic_near_me, "Welcome to My App!");
            case 1:
                return IntroScreenPageFragment.newInstance(R.drawable.ic_connect, "Connect People Near You");
            case 2:
                return IntroScreenPageFragment.newInstance(R.drawable.ic_happy, "Create a Happy Community, Let's get Started");
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }


}
