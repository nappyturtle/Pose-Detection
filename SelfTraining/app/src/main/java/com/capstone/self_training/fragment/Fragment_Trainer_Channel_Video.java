package com.capstone.self_training.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.capstone.self_training.R;
import com.capstone.self_training.adapter.VideoListAdapter;
import com.capstone.self_training.model.Video;
import com.capstone.self_training.service.dataservice.VideoService;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Trainer_Channel_Video extends Fragment {
    private RecyclerView rc_trainer_channel_video;
    private View view;
    private VideoListAdapter videoListAdapter;
    private List<Video> videoList;
    private VideoService videoService;
    private int trainerAccountId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_fragment__trainer__channel__video, container, false);
        init();
        return view;
    }

    private void init() {
        rc_trainer_channel_video = view.findViewById(R.id.rc_trainer_channel_video);
        videoService = new VideoService();
        if (videoList == null) {
            videoList = new ArrayList<>();
        }
        videoList = videoService.getAllFreeVideosByAccount(trainerAccountId);
        if (videoList != null) {
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            rc_trainer_channel_video.setLayoutManager(layoutManager);
            videoListAdapter = new VideoListAdapter(videoList, getContext());
            rc_trainer_channel_video.setAdapter(videoListAdapter);
        }
    }

    public void getTrainerId(int accountId) {
        trainerAccountId = accountId;
    }
}
