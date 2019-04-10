package com.capstone.self_training.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
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
import android.widget.TextView;
import android.widget.Toast;

import com.capstone.self_training.R;
import com.capstone.self_training.adapter.BoughtCourseAdapter;
import com.capstone.self_training.adapter.BoughtTrainerCourseAdapter;
import com.capstone.self_training.adapter.TraineeRegisteredActivityAdapter;
import com.capstone.self_training.adapter.TrainerUploadedCourseAdapter;
import com.capstone.self_training.dto.CourseDTO;
import com.capstone.self_training.dto.EnrollmentDTO;
import com.capstone.self_training.model.Account;
import com.capstone.self_training.model.Course;
import com.capstone.self_training.service.dataservice.CourseService;
import com.capstone.self_training.service.dataservice.EnrollmentService;
import com.capstone.self_training.util.CheckConnection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TrainerUploadedCourseActivity extends AppCompatActivity {

    private RecyclerView traineeRecyclerView;
    private ListView uploadedCourseListView;
    private TrainerUploadedCourseAdapter trainerUploadedCourseAdapter;
    private TraineeRegisteredActivityAdapter traineeRegisteredActivityAdapter;
    private List<CourseDTO> courseList;
    private List<Account> traineeList;
    private int page = 0;
    private int size = 4;
    boolean limitedData = false;
    boolean isLoading = false;
    private LinearLayoutManager linearLayoutManager;
    mHandler mHandler;
    private SharedPreferences mPerferences;
    private int accountId;
    private String token;
    private Toolbar toolbar;
    private TextView txtEmptyUploadedCourse;
    private View progressBar;
    private int checkedSuggestionList = 0;
    private LinearLayout ln_uploadedCourse_isEmpty;
    private TextView uploadedCourse_totalTextView_id;
    private CourseService courseService;
    public static final int REQUEST_CODE_EDIT_COURSE = 0x456;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_uploaded_course);
        if (CheckConnection.haveNetworkConnection(this)) {
            reflect();

            displayToolBar();
            getItemCourse();
            loadDataListView(page,size);
            loadMoreDataListview();
            getAllTrainee();
        } else {
            CheckConnection.showConnection(this,"Kiểm tra kết nối Internet của bạn !!!");
        }
    }

    private void getAllTrainee() {
        uploadedCourse_totalTextView_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ManageTraineeActivity.class);
                intent.putExtra("listManagement", (Serializable) traineeList);
                startActivity(intent);
            }
        });
    }

    private void loadMoreDataListview() {
        uploadedCourseListView.setOnScrollListener(new AbsListView.OnScrollListener() {
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

    private void displayToolBar() {
        toolbar.setTitle("Các khóa hoc đã đăng");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_EDIT_COURSE && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
        }
    }

    private void reflect() {
        toolbar = (Toolbar) findViewById(R.id.trainer_uploaded_course_toolbar_id);
        txtEmptyUploadedCourse = (TextView) findViewById(R.id.txtTrainerUploadedCourseIsEmpty);
        uploadedCourse_totalTextView_id = (TextView) findViewById(R.id.trainerUploadedCourse_totalTextView_id);
        ln_uploadedCourse_isEmpty = (LinearLayout) findViewById(R.id.ln_trainerUploadedCourse_isEmpty);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        progressBar = layoutInflater.inflate(R.layout.progressbar, null);

        // recycler view ( danh sách các trainer của các course đã đăng)
        traineeRecyclerView = (RecyclerView) findViewById(R.id.recycler_trainerUploadedCourse);
        traineeRecyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        //if (traineeList == null)
            traineeList = new ArrayList<>();
        traineeRegisteredActivityAdapter = new TraineeRegisteredActivityAdapter(traineeList, TrainerUploadedCourseActivity.this);
        traineeRecyclerView.setLayoutManager(linearLayoutManager);
        traineeRecyclerView.setAdapter(traineeRegisteredActivityAdapter);

        // listview ( danh sách các course đã mua)
        uploadedCourseListView = (ListView) findViewById(R.id.trainerUploadedCourse_listview);
        //if (courseList == null)
            courseList = new ArrayList<>();
        trainerUploadedCourseAdapter = new TrainerUploadedCourseAdapter(courseList, TrainerUploadedCourseActivity.this);
        uploadedCourseListView.setAdapter(trainerUploadedCourseAdapter);


        mPerferences = PreferenceManager.getDefaultSharedPreferences(this);
        accountId = mPerferences.getInt(getString(R.string.id), 0);
        token = mPerferences.getString(getString(R.string.token), "");
        mHandler = new mHandler();
    }

    // lấy courseId chuyển qua màn hình BoughtVideoListActivity để hiển thị danh sách video theo courseId
    private void getItemCourse() {
        uploadedCourseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(courseList.get(i).getNumberOfVideoInCourse() == 0){
                    Toast.makeText(TrainerUploadedCourseActivity.this, "Không có video nào cả", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(getApplicationContext(), TrainerVideoListActivity.class);
                    intent.putExtra("courseId", courseList.get(i).getCourse().getId());
                    intent.putExtra("trainerId", accountId);
                    startActivity(intent);
                }
            }
        });
    }
    private void loadDataListView(int page, int size) {

        courseService = new CourseService();
        List<CourseDTO> courseDTOSSTemp = courseService.getAllCourseByTrainerId(token, page, size, accountId);

        if (courseDTOSSTemp.size() <= 0 && checkedSuggestionList == 0) { // nếu chưa có course
            // checkedSuggestionList = 0 ở đây có nghĩa là chưa có data
            ln_uploadedCourse_isEmpty.setVisibility(View.INVISIBLE);
            txtEmptyUploadedCourse.setVisibility(View.VISIBLE);
        } else if (courseDTOSSTemp.size() <= 0 && checkedSuggestionList == 1) { // nếu có course nhưng mà load hết dữ liệu
            // checkedSuggestionList = 1 ở đây có nghĩa là khi đã có data nhưng đã load hết rồi
            Log.e("ddasdasdasd <=0 ", "dasdasd <= 0");
            limitedData = true;
            uploadedCourseListView.removeFooterView(progressBar);
            Toast.makeText(this, "Đã hết dữ liệu", Toast.LENGTH_SHORT).show();
        } else {
            uploadedCourseListView.removeFooterView(progressBar);
            for (CourseDTO dto : courseDTOSSTemp) {
                courseList.add(dto);
                trainerUploadedCourseAdapter.notifyDataSetChanged();
            }
            Log.e("ddasdasdasd > 0 ", "dasdasd > 0");
            checkedSuggestionList = 1; // data được load lên thì gán = 1

            traineeList = loadRecyclerView(courseList);
        }
    }
    private List<Account> loadRecyclerView(List<CourseDTO> courseList){
        for(CourseDTO dto : courseList){
            List<Account> accountList = dto.getTraineeList();
            for(Account account : accountList){
                traineeList.add(account);
                traineeRegisteredActivityAdapter.notifyDataSetChanged();
            }
        }
        for (int i = 0; i < traineeList.size(); i++) {
            for (int j = i + 1; j < traineeList.size(); j++) {
                if (traineeList.get(i).getId()== traineeList.get(j).getId()) {
                    traineeList.remove(j);
                    j--;
                }
            }
        }
        return traineeList;
    }


    // handler dùng để quản lí các thread
    // thread này là luồng phụ chạy song song vs luồng chính, dùng để cập nhật số lượng dữ liệu
    public class mHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    uploadedCourseListView.addFooterView(progressBar);
                    break;
                case 1:
                    loadDataListView(++page, size);
                    trainerUploadedCourseAdapter.notifyDataSetChanged();
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

    @Override
    protected void onResume() {
        super.onResume();
        reflect();

        displayToolBar();
        getItemCourse();
        loadDataListView(0,size);
        loadMoreDataListview();
        getAllTrainee();
    }
}
