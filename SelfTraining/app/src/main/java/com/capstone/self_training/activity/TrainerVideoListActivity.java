package com.capstone.self_training.activity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.capstone.self_training.R;
import com.capstone.self_training.adapter.VideoListAdapter;
import com.capstone.self_training.dto.VideoDTO;
import com.capstone.self_training.model.Video;
import com.capstone.self_training.service.dataservice.VideoService;

import java.util.ArrayList;
import java.util.List;

public class TrainerVideoListActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView rcListVideo;
    VideoListAdapter videoListAdapter;
    private List<Video> videos;
    private List<VideoDTO> videoDTOS;
    private VideoService videoService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_video_list);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        toolbar = findViewById(R.id.toolbar_trainer_list_video);
        setupToolbar();

        rcListVideo = findViewById(R.id.rv_trainer_list_video);
        videoService = new VideoService();

        initVideoAdapter();

    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }



    private void initVideoAdapter() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        int accountId = sharedPreferences.getInt(getString(R.string.id), 0);
        if (accountId != 0) {
            videoDTOS = videoService.getVideosByTrainer(accountId);
            if (videos == null) {
                videos = new ArrayList<>();
            }
            if(videoDTOS != null){
                for (VideoDTO v : videoDTOS) {
                    videos.add(v.getVideo());
                }
            }

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            rcListVideo.setLayoutManager(layoutManager);
            videoListAdapter = new VideoListAdapter(videos, getApplicationContext());
            rcListVideo.setAdapter(videoListAdapter);
        }
    }
}
