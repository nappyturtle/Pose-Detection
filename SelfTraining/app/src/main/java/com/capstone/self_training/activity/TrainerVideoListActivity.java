package com.capstone.self_training.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.TextView;

import com.capstone.self_training.R;
import com.capstone.self_training.adapter.TrainerVideoListAdpater;
import com.capstone.self_training.adapter.VideoListAdapter;
import com.capstone.self_training.dto.VideoDTO;
import com.capstone.self_training.model.Video;
import com.capstone.self_training.service.dataservice.VideoService;

import java.util.ArrayList;
import java.util.List;

public class TrainerVideoListActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView rcListVideo;
    TrainerVideoListAdpater videoListAdapter;
    private List<Video> videos;
    private List<VideoDTO> videoDTOS;
    private VideoService videoService;
    public static final int REQUEST_CODE_EDIT_VIDEO = 0x789;
    int courseId;
    int trainerId;
    private SharedPreferences mPerferences;
    String token;
    private TextView txtVideoListTrainerIsEmpty;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_video_list);

//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);

        init();
        getDataRecyclerView(courseId);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_EDIT_VIDEO && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    private void init() {
        Intent intent = getIntent();
        courseId = intent.getIntExtra("courseId",0);
        trainerId = intent.getIntExtra("trainerId",0);
        toolbar = findViewById(R.id.toolbar_trainer_list_video);
        rcListVideo = findViewById(R.id.rv_trainer_list_video);
        mPerferences = PreferenceManager.getDefaultSharedPreferences(this);
        token = mPerferences.getString(getString(R.string.token),"");
        txtVideoListTrainerIsEmpty = findViewById(R.id.txtVideoListTrainerIsEmpty);
        if (videos == null) {
            videos = new ArrayList<>();
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rcListVideo.setLayoutManager(layoutManager);
        videoListAdapter = new TrainerVideoListAdpater(videos, trainerId, TrainerVideoListActivity.this);
        rcListVideo.setAdapter(videoListAdapter);
        setupToolbar();
    }
    private void getDataRecyclerView(int courseId){
        videoService = new VideoService();
        videoDTOS = videoService.getAllVideoByCourseIdToEdit(token,courseId);
        if(videoDTOS == null || videoDTOS.size() == 0){
            txtVideoListTrainerIsEmpty.setVisibility(View.VISIBLE);
            rcListVideo.setVisibility(View.INVISIBLE);
        }else {
            if (videoDTOS != null) {
                for (VideoDTO v : videoDTOS) {
                    videos.add(v.getVideo());
                    videoListAdapter.notifyDataSetChanged();
                }
            }
            txtVideoListTrainerIsEmpty.setVisibility(View.INVISIBLE);
            rcListVideo.setVisibility(View.VISIBLE);
        }
    }
}
