package com.capstone.self_training.activity;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.capstone.self_training.R;
import com.capstone.self_training.adapter.HomeCourseAdapter;
import com.capstone.self_training.adapter.HomeVideoAdapter;
import com.capstone.self_training.dto.CourseDTO;
import com.capstone.self_training.dto.VideoDTO;
import com.capstone.self_training.model.Account;
import com.capstone.self_training.model.Course;
import com.capstone.self_training.model.Video;
import com.capstone.self_training.service.dataservice.CourseService;
import com.capstone.self_training.service.dataservice.VideoService;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private String fragment = "";
    private RecyclerView home_course_list;
    private RecyclerView home_video_list;
    private HomeCourseAdapter homeCourseAdapter;
    private List<CourseDTO> courseDTOList;
    private CourseService courseService;
    private VideoService videoService;
    private List<Video> videos;
    private List<Account> accounts;
    private HomeVideoAdapter homeVideoAdapter;
    SharedPreferences mPerferences;
    int accountId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Bundle bundle = getIntent().getExtras();
        fragment = bundle.getString("fragment");
    }

    @Override
    public void onResume() {
        super.onResume();
        if("Fragment_Course".equalsIgnoreCase(fragment)) {
            searchByName(null);
        }
    }
    public void searchByName(View view) {
        EditText editText = findViewById(R.id.search_value);
        if("Fragment_Course".equalsIgnoreCase(fragment)) {
            getCourseView(editText);
        }
        else if("Fragment_Home".equalsIgnoreCase(fragment)) {
            getVideoView(editText, true);
        }
        else if("Fragment_Trending".equalsIgnoreCase(fragment)) {
            getVideoView(editText, false);
        }
    }
    private void getCourseView(EditText editText) {
        mPerferences = PreferenceManager.getDefaultSharedPreferences(this);
        accountId = mPerferences.getInt(getString(R.string.id),0);
        home_course_list = findViewById(R.id.search_list);
        courseService = new CourseService();
        if (courseDTOList == null) {
            courseDTOList = new ArrayList<>();
        }
        courseDTOList = courseService.searchCourseByName(editText.getText().toString().trim(), accountId);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        home_course_list.setLayoutManager(layoutManager);
        homeCourseAdapter = new HomeCourseAdapter(courseDTOList, this, accountId);
        home_course_list.setAdapter(homeCourseAdapter);
    }
    private void getVideoView(EditText editText, boolean isOrderedByDate) {
        home_video_list = findViewById(R.id.search_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        videos = new ArrayList<>();
        accounts = new ArrayList<>();
        videoService = new VideoService();
        List<VideoDTO> videoDTOS;
        if(isOrderedByDate) {
            videoDTOS = videoService.searchVideoOrderByDate(editText.getText().toString().trim());
        }
        else {
            videoDTOS = videoService.searchVideoOrderByView(editText.getText().toString().trim());
        }
        for (VideoDTO dto : videoDTOS) {
            videos.add(dto.getVideo());
            Account account = new Account();
            account.setUsername(dto.getUsername());
            account.setImgUrl(dto.getImgUrl());
            accounts.add(account);
        }
        homeVideoAdapter = new HomeVideoAdapter(videos, this, accounts);
        home_video_list.setLayoutManager(layoutManager);
        home_video_list.setAdapter(homeVideoAdapter);
    }
}
