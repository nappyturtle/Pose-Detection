package com.capstone.self_training.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.capstone.self_training.R;
import com.capstone.self_training.adapter.MainViewPager;
import com.capstone.self_training.fragment.Fragment_Home;
import com.capstone.self_training.fragment.Fragment_Trainer_Channel_Course;
import com.capstone.self_training.fragment.Fragment_Trainer_Channel_Intro;
import com.capstone.self_training.fragment.Fragment_Trainer_Channel_Video;
import com.capstone.self_training.model.Account;


public class TrainerChannelActivity extends AppCompatActivity {

    private Toolbar toolbar_trainer_channel;
    private TabLayout tabLayout_trainer_channel;
    private ViewPager viewPager_trainer_channel;
    private SharedPreferences mPerferences;
    private Account accountTemp;
    private static final int REQUEST_CODE_LOGIN = 0x9345;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_channel);

        init();

    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        init();
//        Toast.makeText(this, "VUVGGGGGGGGGGGG", Toast.LENGTH_SHORT).show();
//    }



//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_CODE_LOGIN && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
//            Toast.makeText(this, "Đã quay lại TrainerChannelActivity", Toast.LENGTH_SHORT).show();
//            init();
//        }
//    }

    private void init() {
        accountTemp = (Account) getIntent().getSerializableExtra("accountTemp");

        mPerferences = PreferenceManager.getDefaultSharedPreferences(this);

        //toolbar
        toolbar_trainer_channel = findViewById(R.id.toolbar_trainer_channel);
        toolbar_trainer_channel.setTitle(accountTemp.getFullname());
        displayToolBar();

        tabLayout_trainer_channel = findViewById(R.id.tabLayout_trainer_channel);
        viewPager_trainer_channel = findViewById(R.id.viewPager_trainer_channel);

        MainViewPager mainViewPager = new MainViewPager(getSupportFragmentManager());

        Fragment_Trainer_Channel_Video fragment_trainer_channel_video = Fragment_Trainer_Channel_Video.newInstance
                (accountTemp.getId(), mPerferences.getInt(getString(R.string.id), 0), mPerferences.getInt(getString(R.string.roleId), 0));
        //fragment_trainer_channel_video.getTrainerId(trainerId);
        mainViewPager.addFragment(fragment_trainer_channel_video, "Video", TrainerChannelActivity.this);

        Fragment_Trainer_Channel_Course fragment_trainer_channel_course = Fragment_Trainer_Channel_Course.newInstance(accountTemp.getId(),
                mPerferences.getInt(getString(R.string.id), 0),mPerferences.getString(getString(R.string.token),""));
        //fragment_trainer_channel_course.getTrainerId(trainerId, mPerferences.getInt(getString(R.string.id), 0));
        mainViewPager.addFragment(fragment_trainer_channel_course, "Khóa học", TrainerChannelActivity.this);

        Fragment_Trainer_Channel_Intro fragment_trainer_channel_intro = new Fragment_Trainer_Channel_Intro();
        fragment_trainer_channel_intro.getTrainerId(accountTemp.getId());
        mainViewPager.addFragment(fragment_trainer_channel_intro, "Giới thiệu", TrainerChannelActivity.this);

        viewPager_trainer_channel.setAdapter(mainViewPager);
        tabLayout_trainer_channel.setupWithViewPager(viewPager_trainer_channel);
        tabLayout_trainer_channel.setTabGravity(TabLayout.GRAVITY_FILL);
    }

    private void displayToolBar() {
        setSupportActionBar(toolbar_trainer_channel);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_trainer_channel.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}
