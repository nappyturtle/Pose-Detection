package com.capstone.self_training.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.capstone.self_training.R;
import com.capstone.self_training.adapter.MainViewPager;
import com.capstone.self_training.fragment.Fragment_Home;
import com.capstone.self_training.fragment.Fragment_Register;
import com.capstone.self_training.fragment.Fragment_Trending;

public class MainActivity_Home extends AppCompatActivity {

    private final int TRAINER_ROLE = 3;
    private final int TRAINEE_ROLE = 4;

    TabLayout tabLayout;
    ViewPager viewPager;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);
        mapping();
        init();


        //toolbar.setLogo(R.drawable.logoyoga);
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

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
    }

    private void mapping() {
        tabLayout = findViewById(R.id.myTabLayout);
        viewPager = findViewById(R.id.myViewPager);
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btnSearch:
                break;
            case R.id.btnProfile:
                int role = 0;
                if (role == TRAINEE_ROLE) {
                    Intent i = new Intent(MainActivity_Home.this, TraineeProfileActivity.class);
                    startActivity(i);
                } else if (role == TRAINER_ROLE){
                    Intent i = new Intent(MainActivity_Home.this, TrainerProfileActivity.class);
                    startActivity(i);
                } else {
                    Intent i = new Intent(MainActivity_Home.this, RegisterActivity.class);
                    startActivity(i);
                }
                break;
            default:

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
