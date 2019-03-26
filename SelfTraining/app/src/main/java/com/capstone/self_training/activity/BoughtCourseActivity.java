package com.capstone.self_training.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.capstone.self_training.R;
import com.capstone.self_training.adapter.BoughtCourseAdapter;
import com.capstone.self_training.adapter.BoughtTrainerCourseAdapter;
import com.capstone.self_training.adapter.SuggestionAdapter;
import com.capstone.self_training.dto.EnrollmentDTO;
import com.capstone.self_training.model.Account;
import com.capstone.self_training.model.Suggestion;
import com.capstone.self_training.service.dataservice.EnrollmentService;
import com.capstone.self_training.util.CheckConnection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BoughtCourseActivity extends AppCompatActivity {

    private RecyclerView trainerRecyclerView;
    private ListView boughtCourseListView;
    private BoughtCourseAdapter boughtCourseAdapter;
    private BoughtTrainerCourseAdapter boughtTrainerCourseAdapter;
    private List<EnrollmentDTO> enrollmentList;
    private List<Account> trainerCourseList;
    private EnrollmentService enrollmentService;
    private int page = 0;
    private int size = 5;
    boolean limitedData = false;
    boolean isLoading = false;
    private LinearLayoutManager linearLayoutManager;
    mHandler mHandler;
    private SharedPreferences mPerferences;
    private SharedPreferences.Editor mEditor;
    private int accountId;
    private String token;
    private Toolbar toolbar;
    private TextView txtEmptyBoughtCourse;
    private View progressBar;
    private int checkedSuggestionList = 0;
    private LinearLayout ln_boughtCourse_isEmpty;
    private TextView bought_course_totalTextView_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bought_course);

        if (CheckConnection.haveNetworkConnection(this)) {
            init();
            page = 0;
            size = 5;
            loadDataListView(page, size);
            loadDataRecylcerView();
            displayToolBar();
            loadmoreDataListView();
            getItemCourse();
            getTotalTraineeAndTrainer();
        } else {
            CheckConnection.showConnection(this, "Kiểm tra kết nối internet");
        }
    }

    // lấy list trainer của những course đã mua chuyển qua màn hình ManageTraineeTrainerActivity
    private void getTotalTraineeAndTrainer() {
        bought_course_totalTextView_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ManageTrainerActivity.class);
                intent.putExtra("listManagement", (Serializable) trainerCourseList);
                startActivity(intent);
            }
        });
    }


    private void init() {
        toolbar = (Toolbar) findViewById(R.id.bought_course_toolbar_id);
        txtEmptyBoughtCourse = (TextView) findViewById(R.id.txtBoughtCourseIsEmpty);
        bought_course_totalTextView_id = (TextView) findViewById(R.id.bought_course_totalTextView_id);
        ln_boughtCourse_isEmpty = (LinearLayout) findViewById(R.id.ln_boughtCourse_isEmpty);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        progressBar = layoutInflater.inflate(R.layout.progressbar, null);

        // recycler view ( danh sách các trainer của các course đã mua)
        trainerRecyclerView = (RecyclerView) findViewById(R.id.recycler_trainerCourse);
        trainerRecyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        trainerCourseList = new ArrayList<>();
        boughtTrainerCourseAdapter = new BoughtTrainerCourseAdapter(trainerCourseList, getApplicationContext());
        trainerRecyclerView.setLayoutManager(linearLayoutManager);
        trainerRecyclerView.setAdapter(boughtTrainerCourseAdapter);

        // listview ( danh sách các course đã mua)
        boughtCourseListView = (ListView) findViewById(R.id.bought_course_listview);
        enrollmentList = new ArrayList<>();
        boughtCourseAdapter = new BoughtCourseAdapter(enrollmentList, getApplicationContext());
        boughtCourseListView.setAdapter(boughtCourseAdapter);


        mPerferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPerferences.edit();
        accountId = mPerferences.getInt(getString(R.string.id), 0);
        token = mPerferences.getString(getString(R.string.token), "");
        mHandler = new mHandler();
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

    // hiển thi danh sách các course đã mua
    private void loadDataListView(int page, int size) {

        enrollmentService = new EnrollmentService();
        List<EnrollmentDTO> enrollmentDTOSTemp = enrollmentService.getBoughtCoursesByDate(token, page, size, accountId);

        if (enrollmentDTOSTemp.size() <= 0 && checkedSuggestionList == 0) { // nếu chưa có course
            // checkedSuggestionList = 0 ở đây có nghĩa là chưa có data
            ln_boughtCourse_isEmpty.setVisibility(View.INVISIBLE);
            txtEmptyBoughtCourse.setVisibility(View.VISIBLE);
        } else if (enrollmentDTOSTemp.size() <= 0 && checkedSuggestionList == 1) { // nếu có course nhưng mà load hết dữ liệu
            // checkedSuggestionList = 1 ở đây có nghĩa là khi đã có data nhưng đã load hết rồi
            Log.e("ddasdasdasd <=0 ", "dasdasd <= 0");
            limitedData = true;
            boughtCourseListView.removeFooterView(progressBar);
            Toast.makeText(this, "Đã hết dữ liệu", Toast.LENGTH_SHORT).show();
        } else {
            boughtCourseListView.removeFooterView(progressBar);
            for (EnrollmentDTO dto : enrollmentDTOSTemp) {
                enrollmentList.add(dto);
                boughtCourseAdapter.notifyDataSetChanged();
            }
            Log.e("ddasdasdasd > 0 ", "dasdasd > 0");

            checkedSuggestionList = 1; // data được load lên thì gán = 1
        }

    }

    // để hiển thị danh sách những trainer của những course đã mua
    private void loadDataRecylcerView() {
        enrollmentService = new EnrollmentService();
        List<Account> accountList = enrollmentService.getAllTrainerOfBoughtCourse(token, accountId);
        for (Account dto : accountList) {
            trainerCourseList.add(dto);
            boughtTrainerCourseAdapter.notifyDataSetChanged();
        }
    }

    private void loadmoreDataListView() {
        boughtCourseListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
//                    isLoading = false;
//                }
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

    // lấy courseId chuyển qua màn hình BoughtVideoListActivity để hiển thị danh sách video theo courseId
    private void getItemCourse() {
        boughtCourseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), BoughtVideoListActivity.class);
                intent.putExtra("courseId", enrollmentList.get(i).getCourseId());
                startActivity(intent);
            }
        });
    }


    // handler dùng để quản lí các thread
    // thread này là luồng phụ chạy song song vs luồng chính, dùng để cập nhật số lượng dữ liệu
    public class mHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    boughtCourseListView.addFooterView(progressBar);
                    break;
                case 1:
                    loadDataListView(++page, size);
                    boughtCourseAdapter.notifyDataSetChanged();
                    isLoading = false;
                    //progressBar.setVisibility(View.GONE);
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
