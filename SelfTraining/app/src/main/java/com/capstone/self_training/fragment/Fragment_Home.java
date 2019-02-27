
package com.capstone.self_training.fragment;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.capstone.self_training.R;
import com.capstone.self_training.activity.EndlessRecyclerViewScrollListener;
import com.capstone.self_training.adapter.HomeVideoAdapter;
import com.capstone.self_training.dto.VideoDTO;
import com.capstone.self_training.model.Account;
import com.capstone.self_training.model.Video;
import com.capstone.self_training.service.dataservice.VideoService;
import com.capstone.self_training.service.iService.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Home extends Fragment {

    private View view;
    private RecyclerView home_video_list;
    private RecyclerView.Adapter homeVideoAdapter;
    private List<Video> videos;
    private List<Account> accounts;
    private VideoService videoService;

    boolean isLoading = false;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        home_video_list = (RecyclerView) view.findViewById(R.id.home_video_list);
        home_video_list.setHasFixedSize(true);

        videos = new ArrayList<>();
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        home_video_list.setLayoutManager(linearLayoutManager);
        accounts = new ArrayList<>();

        loadData(0,5);

//        home_video_list.setOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                int total = linearLayoutManager.getItemCount();
//                int firstVisibleItemCount = linearLayoutManager.findFirstVisibleItemPosition();
//                int lastVisibleItemCount = linearLayoutManager.findLastVisibleItemPosition();
//                if(!isLoading && firstVisibleItemCount + lastVisibleItemCount == total){
//
//                }
//            }
//        });

        return view;
    }


    private void loadData(int page,int size){

        videoService = new VideoService();
        List<VideoDTO> videoDTOS = videoService.getVideosByDate(page,size);

        for (VideoDTO dto : videoDTOS) {
            videos.add(dto.getVideo());
            Account account = new Account();
            account.setUsername(dto.getUsername());
            account.setImgUrl(dto.getImgUrl());
            accounts.add(account);
        }

        homeVideoAdapter = new HomeVideoAdapter(videos, getContext(), accounts);

        home_video_list.setAdapter(homeVideoAdapter);
    }

}
