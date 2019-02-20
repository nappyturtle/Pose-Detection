package com.example.pdst.Activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.pdst.Adapter.MainViewPager;
import com.example.pdst.Fragment.Fragment_Home;
import com.example.pdst.Fragment.Fragment_Register;
import com.example.pdst.Fragment.Fragment_Trending;
import com.example.pdst.R;

public class MainActivity extends AppCompatActivity {

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
