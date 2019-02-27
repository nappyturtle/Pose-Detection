
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

import java.util.ArrayList;
import java.util.List;

public class Fragment_Trending extends Fragment {

    private View view;
    private RecyclerView trending_video_list;
    private RecyclerView.Adapter trendingVideoAdapter;
    private List<Video> videos;
    private List<Account> accounts;
    private VideoService videoService;
    private EndlessRecyclerViewScrollListener scrollListener;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_trending, container, false);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        trending_video_list = (RecyclerView) view.findViewById(R.id.trending_video_list);
        trending_video_list.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        trending_video_list.setLayoutManager(linearLayoutManager);

        videos = new ArrayList<>();
        accounts = new ArrayList<>();

        loadData(0,5);
//        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
//            @Override
//            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
//                Log.e("load more data ",page + " - "+totalItemsCount);
//                loadData(page,totalItemsCount);
//            }
//        };
//        trending_video_list.addOnScrollListener(scrollListener);

        return view;
    }
    private void loadData(int page,int size){

        videoService = new VideoService();
        List<VideoDTO> videoDTOS = videoService.getAllVideosByTopNumOfView(page,size);

        for (VideoDTO dto : videoDTOS) {
            videos.add(dto.getVideo());
            Account account = new Account();
            account.setUsername(dto.getUsername());
            account.setImgUrl(dto.getImgUrl());
            accounts.add(account);
        }

        trendingVideoAdapter = new HomeVideoAdapter(videos, getContext(), accounts);

        trending_video_list.setAdapter(trendingVideoAdapter);
    }

}
