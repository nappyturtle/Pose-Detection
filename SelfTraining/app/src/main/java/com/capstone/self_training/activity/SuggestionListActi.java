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
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.capstone.self_training.R;
import com.capstone.self_training.adapter.SuggestionAdapter;
import com.capstone.self_training.model.Suggestion;
import com.capstone.self_training.service.dataservice.DataService;
import com.capstone.self_training.service.dataservice.SuggestionService;
import com.capstone.self_training.util.CheckConnection;
import com.capstone.self_training.util.Constants;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SuggestionListActi extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView listView;
    private ArrayList<Suggestion> suggestionList;
    private SuggestionAdapter suggestionAdapter;
    private TextView txtSuggestionIsEmpty;
    View footerView; // progressbar
    //boolean isLoading = false;
    //MHandler mHandler;
    private SharedPreferences mPerferences;
    private SharedPreferences.Editor mEditor;

    private String token;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion_list);
        if (CheckConnection.haveNetworkConnection(this)) {
            reflect();
            displayToolBar();
            getSuggestionItem();
            getData();
            //getMoreData();
        } else {
            CheckConnection.showConnection(this, "Xin vui lòng kiểm tra kết nối internet !!! ");
            finish();
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

    private void getData() {
//        SuggestionService suggestionService = DataService.getSuggestionService();
//        Call<List<Suggestion>> callBack = suggestionService.getSuggestionList(token, id);
//        callBack.enqueue(new Callback<List<Suggestion>>() {
//            @Override
//            public void onResponse(Call<List<Suggestion>> call, Response<List<Suggestion>> response) {
//                if (response.code() == Constants.Status_Ok) {
//                    suggestionList = (ArrayList<Suggestion>) response.body();
//                    if(suggestionList.size() == 0){
//                        // hiển thị textview
//                    }else {
//                        suggestionAdapter = new SuggestionAdapter(getApplicationContext(), R.layout.suggestion_list_item, suggestionList);
//                        listView.setAdapter(suggestionAdapter);
//                    }
//                }else{
//                    Toast.makeText(SuggestionListActi.this, "Hệ thống bị đang bị lỗi, Vui lòng quay lại sau", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Suggestion>> call, Throwable t) {
//                Toast.makeText(SuggestionListActi.this, "Hệ thống bị đang bị lỗi "+t.getMessage(), Toast.LENGTH_SHORT).show();
//                Log.e("SuggestionListActi onFailure: ",t.getMessage());
//            }
//        });
        SuggestionService suggestionService = new SuggestionService();
        suggestionList = (ArrayList<Suggestion>) suggestionService.getSuggestionList(token, id);
        //showMessageIsEmpty();
        //suggestionAdapter.notifyDataSetChanged();
        if(suggestionList.size() <= 0){
            txtSuggestionIsEmpty.setVisibility(View.VISIBLE);
            listView.setVisibility(View.INVISIBLE);
        }else {
            txtSuggestionIsEmpty.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.VISIBLE);
            suggestionAdapter = new SuggestionAdapter(getApplicationContext(), R.layout.suggestion_list_item, suggestionList);
            listView.setAdapter(suggestionAdapter);
        }
    }
    private void showMessageIsEmpty(){
        if(suggestionList.size() <= 0){
            suggestionAdapter.notifyDataSetChanged();
            txtSuggestionIsEmpty.setVisibility(View.VISIBLE);
            listView.setVisibility(View.INVISIBLE);

        }else{
            suggestionAdapter.notifyDataSetChanged();
            txtSuggestionIsEmpty.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.VISIBLE);

        }
    }

    private void reflect() {
        //mHandler = new MHandler();
        toolbar = (Toolbar) findViewById(R.id.suggestionList_toolbar_id);
        listView = (ListView) findViewById(R.id.suggestionList_listview);
        suggestionList = new ArrayList<>();
        suggestionAdapter = new SuggestionAdapter(getApplicationContext(), R.layout.suggestion_list_item, suggestionList);
        listView.setAdapter(suggestionAdapter);
        txtSuggestionIsEmpty = (TextView) findViewById(R.id.txtSuggestionIsEmpty);


        mPerferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPerferences.edit();

        token = mPerferences.getString(getString(R.string.token), "");
        id = mPerferences.getInt(getString(R.string.id), 0);
    }

//    public void getMoreData() {
//        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0 && isLoading == false) {
//                    isLoading = true;
//                    ThreadData threadData = new ThreadData();
//                    threadData.start();
//                }
//            }
//        });
//    }
//
//    public class MHandler extends Handler { // nó là ông chủ dùng để quản lí thread
//        @Override
//        public void handleMessage(Message msg) { // phương thức handleMessage này trả gồm 2 trường hợp
//            // 0: khi bắt đầu thì listview sẽ hiển thị kèm theo đó là progressbar
//            // 1: khi load thêm dữ liệu
//            switch (msg.what) {
//                case 0:
//                    listView.addFooterView(footerView);
//                    break;
//                case 1:
//
//                    isLoading = false;
//
//                    break;
//            }
//            super.handleMessage(msg);
//        }
//    }
//
//    public class ThreadData extends Thread {
//        @Override
//        public void run() {
//            mHandler.sendEmptyMessage(0); // gửi 0 là vì lần đầu chay để lấy dữ liệu
//            try {
//                Thread.sleep(3000); // cho thằng progressbar nó ngủ 3 giây
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            Message message = mHandler.obtainMessage(1); // nó là 1 phương thức liên kết thread vs hanlder
//            // dùng để duy trì kết nối khi muốn gọi lại, tham số truyền vào là 1 thì nó sẽ chuyển vào cái switch case 1
//            mHandler.sendMessage(message);
//            super.run();
//        }
//    }
}
