package com.capstone.self_training.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.capstone.self_training.R;
import com.capstone.self_training.adapter.BoughtVideoAdapter;
import com.capstone.self_training.dto.VideoDTO;
import com.capstone.self_training.fragment.Fragment_Home;
import com.capstone.self_training.model.Account;
import com.capstone.self_training.model.Suggestion;
import com.capstone.self_training.model.Video;
import com.capstone.self_training.service.dataservice.SuggestionService;
import com.capstone.self_training.service.dataservice.VideoService;
import com.capstone.self_training.util.CheckConnection;

import java.util.ArrayList;
import java.util.List;

public class BoughtVideoListActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView listVideo;
    private List<Video> videos;
    private List<Account> accounts;
    private VideoService videoService;
    private int page = 0;
    private int size = 5;
    boolean limitedData = false;
    boolean isLoading = false;
    private BoughtVideoAdapter boughtVideoAdapter;
    private String token;
    private int courseId;
    mHandler mHandler;

    private SharedPreferences mPerferences;
    private SharedPreferences.Editor mEditor;
    private View progressBar;
    private int checkedSuggestionList = 0;
    private TextView txtBoughtVideoIsEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bought_video_list);
        if (CheckConnection.haveNetworkConnection(this)) {
            init();
            displayToolBar();
            getCourseId();
            loadVideoListview(page, size);
            getItemVideo();
            loadmoreVideoListView();
        } else {
            CheckConnection.showConnection(this, "Kiểm tra internet của bạn!!!");
        }
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

    private void loadmoreVideoListView() {
        listVideo.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isLoading = false;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount == totalItemCount && !isLoading && !limitedData & totalItemCount != 0) {
                    isLoading = true;
                    ThreadData threadData = new ThreadData();
                    threadData.start();
                }
            }
        });
    }

    private void getItemVideo() {
        listVideo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), PlayBoughtVideoActivity.class);
                intent.putExtra("PLAYVIDEO", videos.get(i));
                intent.putExtra("ACCOUNT", accounts.get(i));
                startActivity(intent);
            }
        });
    }

    private void getCourseId() {
        Intent intent = getIntent();
        courseId = intent.getIntExtra("courseId",0);
        Log.e("CourseId BoughtVideoListActivity = ", String.valueOf(courseId));
    }

    private void loadVideoListview(int page, int size) {
        videoService = new VideoService();

        List<VideoDTO> videoDTOS = videoService.getAllBoughtVideosByCourseId(token, page, size, courseId);
        if (videoDTOS.size() <= 0 && checkedSuggestionList == 0) {
            listVideo.setVisibility(View.INVISIBLE);
            txtBoughtVideoIsEmpty.setVisibility(View.VISIBLE);
        } else if (videoDTOS.size() <= 0 && checkedSuggestionList == 1) {
            Log.e("ddasdasdasd <=0 ", "dasdasd <= 0");
            limitedData = true;
            listVideo.removeFooterView(progressBar);
            Toast.makeText(this, "Đã hết dữ liệu", Toast.LENGTH_SHORT).show();
        } else {
            listVideo.removeFooterView(progressBar);
            for (VideoDTO dto : videoDTOS) {
                videos.add(dto.getVideo());
                Account account = new Account();
                account.setUsername(dto.getUsername());
                account.setImgUrl(dto.getImgUrl());
                accounts.add(account);
                boughtVideoAdapter.notifyDataSetChanged();
            }
            Log.e("ddasdasdasd > 0 ", "dasdasd > 0");

            checkedSuggestionList = 1;
        }

    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.boughtVideo_toolbar_id);
        listVideo = (ListView) findViewById(R.id.boughtVideo_listview_id);
        txtBoughtVideoIsEmpty = (TextView) findViewById(R.id.txtBoughtVideoIsEmpty);
        videos = new ArrayList<>();
        accounts = new ArrayList<>();
        boughtVideoAdapter = new BoughtVideoAdapter(videos, getApplicationContext());
        listVideo.setAdapter(boughtVideoAdapter);

        mPerferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPerferences.edit();

        token = mPerferences.getString(getString(R.string.token), "");

        mHandler = new mHandler();
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        progressBar = layoutInflater.inflate(R.layout.progressbar, null);
    }

    // handler dùng để quản lí các thread
    // thread này là luồng phụ chạy song song vs luồng chính, dùng để cập nhật số lượng dữ liệu
    public class mHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    listVideo.addFooterView(progressBar);
                    break;
                case 1:
                    loadVideoListview(++page, size);
                    boughtVideoAdapter.notifyDataSetChanged();
                    isLoading = false;
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
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Message message = mHandler.obtainMessage(1);
            mHandler.sendMessage(message);
            super.run();
        }
    }
}
