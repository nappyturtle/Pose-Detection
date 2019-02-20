package com.capstone.self_training.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.capstone.self_training.R;
import com.capstone.self_training.adapter.MainViewPager;
import com.capstone.self_training.fragment.Fragment_Home;
import com.capstone.self_training.fragment.Fragment_Register;
import com.capstone.self_training.fragment.Fragment_Trending;

public class MainActivity_Home extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);
        mapping();
        init();
    }

    private void init() {
        MainViewPager mainViewPager = new MainViewPager(getSupportFragmentManager());
        mainViewPager.addFragment(new Fragment_Home(), "Home");
        mainViewPager.addFragment(new Fragment_Trending(), "Trending");
        mainViewPager.addFragment(new Fragment_Register(), "Register");
        viewPager.setAdapter(mainViewPager);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_top);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_register);

    }

    private void mapping() {
        tabLayout = findViewById(R.id.myTabLayout);
        viewPager = findViewById(R.id.myViewPager);
    }
    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

}
