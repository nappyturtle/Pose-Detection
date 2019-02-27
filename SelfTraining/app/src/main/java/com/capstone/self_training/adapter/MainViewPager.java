package com.capstone.self_training.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.Toast;

import com.capstone.self_training.activity.MainActivity_Home;

import java.util.ArrayList;

public class MainViewPager extends FragmentPagerAdapter {
    private ArrayList<Fragment> arrayFragment = new ArrayList<>();
    private ArrayList<String> arrayTitle = new ArrayList<>();

    public MainViewPager(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return arrayFragment.get(position);
    }

    @Override
    public int getCount() {
        return arrayFragment.size();
    }
    public void addFragment(Fragment fragment, String title,Context context){
//        if(fragment == null){
//            Toast.makeText(context, "App hiện tại đang có vấn đề, vui lòng quay lại sau", Toast.LENGTH_SHORT).show();
//        }else {
            arrayFragment.add(fragment);
            arrayTitle.add(title);
        //}
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return arrayTitle.get(position);
    }
}
