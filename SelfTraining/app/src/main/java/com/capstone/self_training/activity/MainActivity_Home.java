package com.capstone.self_training.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.capstone.self_training.R;
import com.capstone.self_training.adapter.MainViewPager;
import com.capstone.self_training.fragment.Fragment_Home;
import com.capstone.self_training.fragment.Fragment_Course;
import com.capstone.self_training.fragment.Fragment_Trending;
import com.capstone.self_training.util.CheckConnection;

import java.util.List;

public class MainActivity_Home extends AppCompatActivity {

    private final int TRAINER_ROLE = 3;
    private final int TRAINEE_ROLE = 4;
    private String currentFragment = "Fragment_Home";
    TabLayout tabLayout;
    ViewPager viewPager;
    private Toolbar toolbar;
    private SharedPreferences mPerferences;
    private SharedPreferences.Editor mEditor;
    private MainViewPager mainViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);
        if (CheckConnection.haveNetworkConnection(this)) {
            mapping();
            init();
            changePager();
        } else {
            CheckConnection.showConnection(this, "Xin vui lòng kiểm tra kết nối internet !!! ");
            finish();
        }


        //toolbar.setLogo(R.drawable.logoyoga);
    }

    private void displayToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void init() {
        mainViewPager = new MainViewPager(getSupportFragmentManager());

        mainViewPager.addFragment(new Fragment_Home(), "Trang chủ", getApplicationContext());
        mainViewPager.addFragment(new Fragment_Trending(), "Thịnh hành", getApplicationContext());
        mainViewPager.addFragment(new Fragment_Course(), "Khoá học", getApplicationContext());

        viewPager.setAdapter(mainViewPager);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_top);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_register);

        toolbar = (Toolbar) findViewById(R.id.toolbar_main_home);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Self-Training");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setLogo(R.drawable.ic_logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);


        mPerferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPerferences.edit();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Fragment fragment = mainViewPager.getItem(position);
                String fragmentName = fragment.getClass().toString();
                currentFragment = fragmentName.substring(fragmentName.lastIndexOf(".") + 1);
                Log.e("currentFragment = ", currentFragment);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
    private void changePager(){
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Fragment fragment = mainViewPager.getItem(position);
                String fragmentName = fragment.getClass().toString();
                currentFragment = fragmentName.substring(fragmentName.lastIndexOf(".") + 1);
                Log.e("currentFragment = ", currentFragment);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //Toast.makeText(MainActivity_Home.this, "onPageScrollStateChanged " + state, Toast.LENGTH_SHORT).show();
            }
        });
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
        int roleId = mPerferences.getInt(getString(R.string.roleId), 0);
        switch (item.getItemId()) {
            case R.id.btnSearch:
                //Search
                Intent intent = new Intent(MainActivity_Home.this, SearchActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("fragment", currentFragment);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.btnProfile:
                if (roleId == TRAINEE_ROLE) {
                    Intent i = new Intent(MainActivity_Home.this, TraineeProfileActivity.class);
                    startActivity(i);
                } else if (roleId == TRAINER_ROLE) {
                    Intent i = new Intent(MainActivity_Home.this, TrainerProfileActivity.class);
                    startActivity(i);
                } else {
                    Intent i = new Intent(MainActivity_Home.this, LoginActivity.class);
                    startActivity(i);
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
