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
    private int traineeId;
    private int courseId;
    private boolean checked;
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

            Intent intent = getIntent();
            courseId = intent.getIntExtra("courseId", 0);
            checked = intent.getBooleanExtra("checked", true);
            if (checked) {
                loadVideoBoughtListview(page, size);
                loadmoreVideoCourseBoughtListView();
            } else {
                loadVideoCourseUnBoughtListview(page, size);
                loadmoreVideoCourseUnBoughtListView();
            }
            Log.e("CourseId BoughtVideoListActivity = ", String.valueOf(courseId));

            //loadVideoListview();
            getItemVideo();

        } else {
            CheckConnection.showConnection(this, "Kiểm tra internet của bạn!!!");
        }
    }

    // hiển thị thanh toolbar
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

    // lấy thêm data bằng cách scroll nó xuống dưới cuối màn hình
    private void loadmoreVideoCourseBoughtListView() {
        listVideo.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount == totalItemCount && !isLoading && !limitedData & totalItemCount != 0) {
                    isLoading = true;
                    ThreadDataCourseUpdated threadData = new ThreadDataCourseUpdated();
                    threadData.start();
                }
            }
        });
    }

    private void loadmoreVideoCourseUnBoughtListView() {
        listVideo.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount == totalItemCount && !isLoading && !limitedData & totalItemCount != 0) {
                    isLoading = true;
                    ThreadDataCourseUnBought threadData = new ThreadDataCourseUnBought();
                    threadData.start();
                }
            }
        });
    }

    // để lấy thông tin video, account để chuyển qua màn hình PlayBoughtVideoActivity
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

    private void loadVideoBoughtListview(int page, int size) {
        videoService = new VideoService();

        List<VideoDTO> videoDTOS = videoService.getAllBoughtVideosByCourseId(token, page, size, traineeId, courseId);
        if (videoDTOS.size() <= 0 && checkedSuggestionList == 0) {// nếu chưa có course
            // checkedSuggestionList = 0 ở đây có nghĩa là chưa có data
            listVideo.setVisibility(View.INVISIBLE);
            txtBoughtVideoIsEmpty.setVisibility(View.VISIBLE);
        } else if (videoDTOS.size() <= 0 && checkedSuggestionList == 1) { // nếu có course nhưng mà load hết dữ liệu
            // checkedSuggestionList = 1 ở đây có nghĩa là khi đã có data nhưng đã load hết rồi
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

            checkedSuggestionList = 1; // data được load lên thì gán = 1
        }
    }

    private void loadVideoCourseUnBoughtListview(int page, int size) {
        videoService = new VideoService();

        List<VideoDTO> videoDTOS = videoService.getAllUnBoughtVideoByCourseId(token, page, size, traineeId, courseId);
        if (videoDTOS.size() <= 0 && checkedSuggestionList == 0) {// nếu chưa có course
            // checkedSuggestionList = 0 ở đây có nghĩa là chưa có data
            listVideo.setVisibility(View.INVISIBLE);
            txtBoughtVideoIsEmpty.setVisibility(View.VISIBLE);
        } else if (videoDTOS.size() <= 0 && checkedSuggestionList == 1) { // nếu có course nhưng mà load hết dữ liệu
            // checkedSuggestionList = 1 ở đây có nghĩa là khi đã có data nhưng đã load hết rồi
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

            checkedSuggestionList = 1; // data được load lên thì gán = 1
        }
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.boughtVideo_toolbar_id);
        listVideo = (ListView) findViewById(R.id.boughtVideo_listview_id);
        txtBoughtVideoIsEmpty = (TextView) findViewById(R.id.txtBoughtVideoIsEmpty);
        videos = new ArrayList<>();
        accounts = new ArrayList<>();
        boughtVideoAdapter = new BoughtVideoAdapter(videos, BoughtVideoListActivity.this);
        listVideo.setAdapter(boughtVideoAdapter);

        mPerferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPerferences.edit();

        traineeId = mPerferences.getInt(getString(R.string.id), 0);
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
                    loadVideoBoughtListview(++page, size);
                    boughtVideoAdapter.notifyDataSetChanged();
                    isLoading = false;
                    break;
                case 2:
                    loadVideoCourseUnBoughtListview(++page, size);
                    boughtVideoAdapter.notifyDataSetChanged();
                    isLoading = false;
                    break;
            }
            super.handleMessage(msg);
        }
    }

    public class ThreadDataCourseUpdated extends Thread {
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

    public class ThreadDataCourseUnBought extends Thread {
        @Override
        public void run() {
            mHandler.sendEmptyMessage(0);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Message message = mHandler.obtainMessage(2);
            mHandler.sendMessage(message);
            super.run();
        }
    }
}
