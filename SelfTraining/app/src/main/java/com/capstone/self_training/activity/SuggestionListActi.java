package com.capstone.self_training.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.capstone.self_training.R;
import com.capstone.self_training.adapter.SuggestionAdapter;
import com.capstone.self_training.dto.SuggestionDTO;
import com.capstone.self_training.model.Suggestion;
import com.capstone.self_training.service.dataservice.DataService;
import com.capstone.self_training.service.dataservice.SuggestionService;
import com.capstone.self_training.util.CheckConnection;
import com.capstone.self_training.util.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SuggestionListActi extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView listView;
    private List<SuggestionDTO> suggestionList;
    private SuggestionAdapter suggestionAdapter;
    private TextView txtSuggestionIsEmpty;
    private View progressBar;

    private boolean limitedData = false;
    private boolean isLoading = false;

    private SharedPreferences mPerferences;
    private SharedPreferences.Editor mEditor;
    private mHandler mHandler;
    private String token;
    private int id;
    private int page = 0;
    private int size = 5;
    private int checkedSuggestionList = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion_list);

        if (CheckConnection.haveNetworkConnection(this)) {
            reflect();
            displayToolBar();
            getSuggestionItem();
            getData(page, size);
            getMoreData();

        } else {
            CheckConnection.showConnection(this, "Xin vui lòng kiểm tra kết nối internet !!! ");
            finish();
        }

    }
    // hiển thị more data suggestion
    private void getMoreData() {
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
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

    // hiển thị thanh toobar
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

    // lấy id của suggestion để hiển thị suggestion detail
    private void getSuggestionItem() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), SuggestionDetailActi.class);
                intent.putExtra("suggestionId", suggestionList.get(i).getId());
                startActivity(intent);
            }
        });
    }

    private void  getData(int page, int size) {

//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);

        SuggestionService suggestionService = new SuggestionService();
        List<SuggestionDTO> listTemp = suggestionService.getSuggestionList(token, page, size, id);
        if (listTemp.size() <= 0 && checkedSuggestionList == 0) {// nếu chưa có course
            // checkedSuggestionList = 0 ở đây có nghĩa là chưa có data
            listView.setVisibility(View.INVISIBLE);
            txtSuggestionIsEmpty.setVisibility(View.VISIBLE);
        } else
            if (listTemp.size() <= 0 && checkedSuggestionList == 1) { // nếu có course nhưng mà load hết dữ liệu
                // checkedSuggestionList = 1 ở đây có nghĩa là khi đã có data nhưng đã load hết rồi
            Log.e("ddasdasdasd <=0 ", "dasdasd <= 0");
            limitedData = true;
            listView.removeFooterView(progressBar);
                Toast.makeText(this, "Đã hết dữ liệu", Toast.LENGTH_SHORT).show();
        } else {
            listView.removeFooterView(progressBar);
            for (SuggestionDTO su : listTemp) {
                suggestionList.add(su);
                suggestionAdapter.notifyDataSetChanged();
            }
            Log.e("ddasdasdasd > 0 ", "dasdasd > 0");

            checkedSuggestionList = 1;
        }
    }

    private void reflect() {

        toolbar = (Toolbar) findViewById(R.id.suggestionList_toolbar_id);
        listView = (ListView) findViewById(R.id.suggestionList_listview);
        suggestionList = new ArrayList<>();
        suggestionAdapter = new SuggestionAdapter(getApplicationContext(), suggestionList);
        listView.setAdapter(suggestionAdapter);
        txtSuggestionIsEmpty = (TextView) findViewById(R.id.txtSuggestionIsEmpty);


        mPerferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPerferences.edit();

        token = mPerferences.getString(getString(R.string.token), "");
        id = mPerferences.getInt(getString(R.string.id), 0);
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
                    listView.addFooterView(progressBar);
                    break;
                case 1:
                    getData(++page, size);
                    suggestionAdapter.notifyDataSetChanged();
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
