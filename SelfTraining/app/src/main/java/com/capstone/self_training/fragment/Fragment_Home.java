
package com.capstone.self_training.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.capstone.self_training.R;
import com.capstone.self_training.activity.MainActivity_Home;
import com.capstone.self_training.adapter.HomeVideoAdapter;
import com.capstone.self_training.dto.VideoDTO;
import com.capstone.self_training.model.Account;
import com.capstone.self_training.model.Video;
import com.capstone.self_training.service.dataservice.VideoService;
import com.capstone.self_training.util.CheckConnection;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Home extends Fragment {

    private View view;
    private RecyclerView home_video_list;
    private HomeVideoAdapter homeVideoAdapter;
    public List<Video> videos;
    public List<Account> accounts;
    private VideoService videoService;
    private int page;
    private int size;
    boolean limitedData;
    boolean isLoading;
    //int currentItem, totalItems, firstItem;
    private LinearLayoutManager linearLayoutManager;
    private ProgressBar progressBar;
    private mHandler mHandler;
    private boolean checkLoading = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        if (CheckConnection.haveNetworkConnection(getContext())) {
            init();
            page = 0;
            size = 3;
            limitedData = false;
            isLoading = false;
            loadData(page, size);
            loadMoreData();

            return view;
        } else {
            CheckConnection.showConnection(getContext(), "Kiểm tra kết nối internet");
        }
        return null;

    }
    @Override
    public void onResume() {
        super.onResume();
        init();
        loadData(0, size);
        loadMoreData();
    }
    private void loadMoreData() {
        mHandler = new mHandler();
        home_video_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isLoading = false;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //Toast.makeText(getContext(), "da vao cho nay", Toast.LENGTH_SHORT).show();
                int currentItem = linearLayoutManager.getChildCount(); // phần từ nhìn nhìn thấy dc
                int totalItems = linearLayoutManager.getItemCount();   // tổng số phần tử
                int firstItem = linearLayoutManager.findFirstVisibleItemPosition(); // phần tử đầu tiên
                if (!isLoading && currentItem + firstItem == totalItems && totalItems != 0 && !limitedData) {
                    // currentItem + firstItem == totalItems ( phần tử đầu tiên + số phần tử nhìn thấy dc => thì có nghĩa nó đang
                    // đứng ở vị trí cuối cùng => bật progress bar lên
                    isLoading = true;
                    ThreadData threadData = new ThreadData();
                    threadData.start();
                }
            }
        });
    }

    private void init() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        home_video_list = (RecyclerView) view.findViewById(R.id.home_video_list);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar_id_home);
        home_video_list.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getContext());
        videos = new ArrayList<>();
        accounts = new ArrayList<>();

        homeVideoAdapter = new HomeVideoAdapter(videos, getContext(), accounts);
        home_video_list.setAdapter(homeVideoAdapter);
        home_video_list.setLayoutManager(linearLayoutManager);


    }

    private void loadData(int page, int size) {
        videoService = new VideoService();
        Log.e("pageHome = ", String.valueOf(page));
        List<VideoDTO> videoDTOS = videoService.getVideosByDate(page, size);
        if (videoDTOS.size() <= 0) {
            limitedData = true;
            CheckConnection.showConnection(getContext(), "Đã hết dữ liệu");
            progressBar.setVisibility(View.GONE);

        } else {
            progressBar.setVisibility(View.GONE);
            for (VideoDTO dto : videoDTOS) {
                videos.add(dto.getVideo());
                Account account = new Account();
                account.setUsername(dto.getUsername());
                account.setImgUrl(dto.getImgUrl());
                account.setId(dto.getAccountId());
                accounts.add(account);
                //homeVideoAdapter.notifyDataSetChanged();
            }
        }
    }

    // handler dùng để quản lí các thread
    // thread này là luồng phụ chạy song song vs luồng chính, dùng để cập nhật số lượng dữ liệu
    public class mHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    progressBar.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    loadData(++page, size);
                    homeVideoAdapter.notifyDataSetChanged();
                    isLoading = false;
                    progressBar.setVisibility(View.GONE);
                    break;
            }

            super.handleMessage(msg);
        }
    }

    public class ThreadData extends Thread {
        @Override
        public void run() {
            mHandler.sendEmptyMessage(0);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Message message = mHandler.obtainMessage(1);
            mHandler.sendMessage(message);
            super.run();
        }
    }

}


