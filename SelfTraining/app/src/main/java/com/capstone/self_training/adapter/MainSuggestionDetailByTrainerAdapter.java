package com.capstone.self_training.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.capstone.self_training.fragment.SuggestionDetailFragment;
import com.capstone.self_training.fragment.SuggestionDetailFragmentByTrainer;

import java.util.ArrayList;

public class MainSuggestionDetailByTrainerAdapter extends FragmentStatePagerAdapter {
    private ArrayList<Fragment> arrayListFragment = new ArrayList<>();


    public MainSuggestionDetailByTrainerAdapter(FragmentManager fm) {
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
    public void addFragment(SuggestionDetailFragmentByTrainer fragment){

        arrayListFragment.add(fragment);


    }

}
