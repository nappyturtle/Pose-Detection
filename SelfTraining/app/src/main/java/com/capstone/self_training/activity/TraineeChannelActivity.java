package com.capstone.self_training.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TableLayout;

import com.capstone.self_training.R;
import com.capstone.self_training.adapter.MainViewPager;
import com.capstone.self_training.fragment.Fragment_Trainee_Profile;
import com.capstone.self_training.fragment.Fragment_Trainee_Suggestion;
import com.capstone.self_training.model.Account;
import com.capstone.self_training.util.CheckConnection;

public class TraineeChannelActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    private Toolbar toolbar;
    private Account accountTemp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainee_management);
        if(CheckConnection.haveNetworkConnection(this)){
            Intent intent = getIntent();
            accountTemp = (Account) intent.getSerializableExtra("accountTemp");
            init();
            displayToolBar();
        }else{
            CheckConnection.showConnection(this,"Kiểm tra kết nối Internent!!!");
        }
    }

    private void init() {

        toolbar = (Toolbar) findViewById(R.id.traineeManagement_toolbar_id);
        toolbar.setTitle(accountTemp.getFullname());
        tabLayout = (TabLayout) findViewById(R.id.traineeManagement_tabLayout);
        viewPager = (ViewPager) findViewById(R.id.traineeManagement_myViewPager);

        MainViewPager mainViewPager = new MainViewPager(getSupportFragmentManager());
        // hiển thị màn hinh Fragment_Suggestion
        Fragment_Trainee_Suggestion fragment_suggestion = Fragment_Trainee_Suggestion.newInstance(accountTemp.getId());
        mainViewPager.addFragment(fragment_suggestion, "Các video tập theo",TraineeChannelActivity.this);

        // hiển thị màn hình Fragment_Profile
        Fragment_Trainee_Profile fragment_profile = Fragment_Trainee_Profile.newInstance(accountTemp.getId());
        mainViewPager.addFragment(fragment_profile, "Giới thiệu",TraineeChannelActivity.this);

        viewPager.setAdapter(mainViewPager);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

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
}
