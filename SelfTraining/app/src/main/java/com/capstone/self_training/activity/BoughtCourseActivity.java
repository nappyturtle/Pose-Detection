package com.capstone.self_training.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.capstone.self_training.R;
import com.capstone.self_training.adapter.BoughtCourseAdapter;
import com.capstone.self_training.adapter.BoughtCourseAddedVideoAdapter;
import com.capstone.self_training.adapter.BoughtTrainerCourseAdapter;
import com.capstone.self_training.adapter.BoughtVideoAdapter;
import com.capstone.self_training.adapter.SuggestionAdapter;
import com.capstone.self_training.dto.CourseDTO;
import com.capstone.self_training.dto.EnrollmentDTO;
import com.capstone.self_training.model.Account;
import com.capstone.self_training.model.Suggestion;
import com.capstone.self_training.model.Video;
import com.capstone.self_training.service.dataservice.EnrollmentService;
import com.capstone.self_training.util.CheckConnection;

import java.io.Serializable;
import java.text.DecimalFormat;
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
                Intent intent = new Intent(getApplicationContext(), ManageTrainerActivity.class);
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

    /**
     * @author KietPT
     * @since 11/4/2019
         hiển thi danh sách các course đã mua
     */
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

    /**
     * @author KietPT
     * @since 11/4/2019
        để hiển thị danh sách những trainer của những course đã mua
     */
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

    /**
     * @author KietPT
     * @since 11/4/2019
    lấy courseId chuyển qua màn hình BoughtVideoListActivity để hiển thị danh sách video theo courseId
    kiểm tra xem là course này trainee đã update(thêm tiền) chưa
            - nếu chưa thì hiển thị danh sách video mới dc cập nhật
                + nếu cập nhật thì sẽ qua màn hình mua course
                + nếu hủy thì sẽ qua màn hình những video cũ( ko có video mới )
            - nếu rồi thì sẽ chuyển qua màn hình hiển thị danh sách tất cả video của course đó
     */
    private void getItemCourse() {
        boughtCourseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                EnrollmentService enrollmentService = new EnrollmentService();
                CourseDTO dto = enrollmentService.checkBoughtCourseUpdatedByTrainer(token, accountId, enrollmentList.get(i).getCourseId());
                if (dto != null && dto.getVideoUpdated() != null) {
                    displayDialogVideoUpdated(dto);
                } else {
                    Intent intent = new Intent(getApplicationContext(), BoughtVideoListActivity.class);
                    intent.putExtra("courseId", enrollmentList.get(i).getCourseId());
                    intent.putExtra("checked",true);
                    startActivity(intent);
                }
            }
        });
    }
    public void displayDialogVideoUpdated(CourseDTO dto){
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.item_dialog_new_video_from_bought_course);
        ListView listViewVideoUpdated = (ListView) dialog.findViewById(R.id.listView_video_boughtCourseUpdated);
        Button btnSave = (Button) dialog.findViewById(R.id.btnSave_video_boughtCourseUpdated);
        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel_video_boughtCourseUpdated);
        List<Video> videos = new ArrayList<>();
        for(Video v: dto.getVideoUpdated()){
            videos.add(v);
        }
        BoughtCourseAddedVideoAdapter adapter = new BoughtCourseAddedVideoAdapter(videos,BoughtCourseActivity.this);
        listViewVideoUpdated.setAdapter(adapter);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayDialogCourseUpdated(dto);
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), BoughtVideoListActivity.class);
                intent.putExtra("courseId", dto.getCourse().getId());
                intent.putExtra("checked",false);
                startActivity(intent);
            }
        });
        dialog.show();
    }

    private void displayDialogCourseUpdated(CourseDTO dto) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Danh sách video được cập nhật");

        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        String moneyDots = decimalFormat.format(dto.getCourse().getPrice())+",000 đồng ";

        alertDialog.setMessage("Chỉ cần thêm "+ moneyDots + "để sỡ hữu trọn bộ khóa học này !!!");
        alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(BoughtCourseActivity.this,CourseDetailPayment.class);
                intent.putExtra("courseDTO",dto);
                intent.putExtra("updatedCourse",true);
                startActivity(intent);
            }
        });
        alertDialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        dialog.dismiss();
                    }
                });
            }
        });
        alertDialog.show();
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
