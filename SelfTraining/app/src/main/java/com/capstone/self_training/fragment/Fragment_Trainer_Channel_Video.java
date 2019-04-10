package com.capstone.self_training.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.capstone.self_training.model.Account;
import com.capstone.self_training.model.Video;
import com.capstone.self_training.service.dataservice.AccountService;
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
    private AccountService accountService;
    private Account trainer;
    private int currentUserId;
    private int roleId;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_fragment__trainer__channel__video, container, false);
        savedInstanceState = getArguments();
        currentUserId = savedInstanceState.getInt("currentUserId");
        trainerAccountId = savedInstanceState.getInt("trainerAccountId");
        roleId = savedInstanceState.getInt("roleId");
        init();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    private void init() {
        rc_trainer_channel_video = view.findViewById(R.id.rc_trainer_channel_video);
        videoService = new VideoService();
        if (videoList == null) {
            videoList = new ArrayList<>();
        }
        videoList = videoService.getAllFreeVideosByAccount(trainerAccountId);

        accountService = new AccountService(getContext());
        trainer = accountService.getAccount(trainerAccountId);

        if (videoList != null) {
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            rc_trainer_channel_video.setLayoutManager(layoutManager);
            videoListAdapter = new VideoListAdapter(videoList, getContext(), trainer,currentUserId,roleId);
            rc_trainer_channel_video.setAdapter(videoListAdapter);
        }
    }

//    public void getTrainerId(int accountId) {
//        trainerAccountId = accountId;
//    }

    public static Fragment_Trainer_Channel_Video newInstance(int trainerAccountId, int currentUserId, int roleId) {
        Fragment_Trainer_Channel_Video f = new Fragment_Trainer_Channel_Video();
        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putInt("currentUserId", currentUserId);
        args.putInt("roleId", roleId);
        args.putInt("trainerAccountId", trainerAccountId);
        f.setArguments(args);
        return f;
    }
}
