package com.capstone.self_training.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.capstone.self_training.fragment.SuggestionDetailFragment;

import java.util.ArrayList;

public class MainSuggestionDetailAdapter extends FragmentStatePagerAdapter{
    private ArrayList<Fragment> arrayListFragment = new ArrayList<>();


    public MainSuggestionDetailAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return arrayListFragment.get(position);
    }

    @Override
    public int getCount() {
        return arrayListFragment.size();
    }
    public void addFragment(SuggestionDetailFragment fragment){
        arrayListFragment.add(fragment);


    }

}
