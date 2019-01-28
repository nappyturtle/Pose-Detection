package com.capstone.self_training.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.capstone.self_training.R;
import com.capstone.self_training.adapter.SuggestionAdapter;
import com.capstone.self_training.model.Suggestion;
import com.capstone.self_training.service.apiservice.ApiService;
import com.capstone.self_training.service.dataservice.DataService;
import com.capstone.self_training.util.CheckConnection;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SuggestionListActi extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView listView;
    private ArrayList<Suggestion> suggestionList;
    SuggestionAdapter suggestionAdapter;
    View footerView; // progressbar
    boolean isLoading = false;
    MHandler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion_list);
        if(CheckConnection.haveNetworkConnection(this)){
            reflect();
            displayToolBar();
            getSuggestionItem();
            getMoreData();
        }else{
            CheckConnection.showConnection(this,"Xin vui lòng kiểm tra kết nối internet !!! ");
        }

    }
    private void displayToolBar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void getSuggestionItem(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(),SuggestionDetailActi.class);
                startActivity(intent);
            }
        });
    }
    private void getData(){
        ApiService apiService = DataService.getService();
        Call<List<Suggestion>> suggestionList = apiService.getSuggestionList();
        suggestionList.enqueue(new Callback<List<Suggestion>>() {
            @Override
            public void onResponse(Call<List<Suggestion>> call, Response<List<Suggestion>> response) {
                ArrayList<Suggestion> suggestion = (ArrayList<Suggestion>) response.body();
            }

            @Override
            public void onFailure(Call<List<Suggestion>> call, Throwable t) {

            }
        });
    }
    private void reflect(){
        mHandler = new MHandler();
        toolbar = (Toolbar)findViewById(R.id.suggestionList_toolbar_id);
        listView = (ListView) findViewById(R.id.suggestionList_listview);
        suggestionList = new ArrayList<>();
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(c);
        Toast.makeText(this, "date = "+formattedDate, Toast.LENGTH_SHORT).show();
        suggestionList.add(new Suggestion(1,"Động tác cơ bản dành cho dân văn phòng công sở",formattedDate,"https://firebasestorage.googleapis.com/v0/b/demouploadvideo-bdc04.appspot.com/o/images.jpg?alt=media&token=354c2c23-27f8-4498-8783-faf0c4952afc"));
        suggestionList.add(new Suggestion(2,"Động tác cơ bản dành cho dân văn phòng công sở",formattedDate,"https://firebasestorage.googleapis.com/v0/b/demouploadvideo-bdc04.appspot.com/o/images.jpg?alt=media&token=354c2c23-27f8-4498-8783-faf0c4952afc"));
        suggestionList.add(new Suggestion(3,"Động tác cơ bản dành cho dân văn phòng công sở",formattedDate,"https://firebasestorage.googleapis.com/v0/b/demouploadvideo-bdc04.appspot.com/o/images.jpg?alt=media&token=354c2c23-27f8-4498-8783-faf0c4952afc"));
        suggestionList.add(new Suggestion(4,"Động tác cơ bản dành cho dân văn phòng công sở",formattedDate,"https://firebasestorage.googleapis.com/v0/b/demouploadvideo-bdc04.appspot.com/o/images.jpg?alt=media&token=354c2c23-27f8-4498-8783-faf0c4952afc"));
        suggestionList.add(new Suggestion(5,"Động tác cơ bản dành cho dân văn phòng công sở",formattedDate,"https://firebasestorage.googleapis.com/v0/b/demouploadvideo-bdc04.appspot.com/o/images.jpg?alt=media&token=354c2c23-27f8-4498-8783-faf0c4952afc"));
        suggestionList.add(new Suggestion(6,"Động tác cơ bản dành cho dân văn phòng công sở",formattedDate,"https://firebasestorage.googleapis.com/v0/b/demouploadvideo-bdc04.appspot.com/o/images.jpg?alt=media&token=354c2c23-27f8-4498-8783-faf0c4952afc"));
        suggestionList.add(new Suggestion(7,"Động tác cơ bản dành cho dân văn phòng công sở",formattedDate,"https://firebasestorage.googleapis.com/v0/b/demouploadvideo-bdc04.appspot.com/o/images.jpg?alt=media&token=354c2c23-27f8-4498-8783-faf0c4952afc"));


        suggestionAdapter = new SuggestionAdapter(this,R.layout.suggestion_list_item,suggestionList);
        listView.setAdapter(suggestionAdapter);

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        footerView = inflater.inflate(R.layout.progressbar,null);
    }

    public void getMoreData() {
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0 && isLoading == false){
                    isLoading = true;
                    ThreadData threadData = new ThreadData();
                    threadData.start();
                }
            }
        });
    }
    public class MHandler extends Handler{ // nó là ông chủ dùng để quản lí thread
        @Override
        public void handleMessage(Message msg) { // phương thức handleMessage này trả gồm 2 trường hợp
            // 0: khi bắt đầu thì listview sẽ hiển thị kèm theo đó là progressbar
            // 1: khi load thêm dữ liệu
            switch (msg.what){
                case 0:
                    listView.addFooterView(footerView);
                    break;
                case 1:
                    suggestionList.add(new Suggestion(8,"bài tập cơ bản","12-3-2018","https://firebasestorage.googleapis.com/v0/b/demouploadvideo-bdc04.appspot.com/o/images.jpg?alt=media&token=354c2c23-27f8-4498-8783-faf0c4952afc"));
                    suggestionList.add(new Suggestion(9,"bài tập cơ bản","12-3-2018","https://firebasestorage.googleapis.com/v0/b/demouploadvideo-bdc04.appspot.com/o/images.jpg?alt=media&token=354c2c23-27f8-4498-8783-faf0c4952afc"));
                    suggestionList.add(new Suggestion(10,"bài tập cơ bản","12-3-2018","https://firebasestorage.googleapis.com/v0/b/demouploadvideo-bdc04.appspot.com/o/images.jpg?alt=media&token=354c2c23-27f8-4498-8783-faf0c4952afc"));
                    suggestionList.add(new Suggestion(11,"bài tập cơ bản","12-3-2018","https://firebasestorage.googleapis.com/v0/b/demouploadvideo-bdc04.appspot.com/o/images.jpg?alt=media&token=354c2c23-27f8-4498-8783-faf0c4952afc"));
                    suggestionList.add(new Suggestion(12,"bài tập cơ bản","12-3-2018","https://firebasestorage.googleapis.com/v0/b/demouploadvideo-bdc04.appspot.com/o/images.jpg?alt=media&token=354c2c23-27f8-4498-8783-faf0c4952afc"));
                    suggestionList.add(new Suggestion(13,"bài tập cơ bản","12-3-2018","https://firebasestorage.googleapis.com/v0/b/demouploadvideo-bdc04.appspot.com/o/images.jpg?alt=media&token=354c2c23-27f8-4498-8783-faf0c4952afc"));
                    suggestionList.add(new Suggestion(14,"bài tập cơ bản","12-3-2018","https://firebasestorage.googleapis.com/v0/b/demouploadvideo-bdc04.appspot.com/o/images.jpg?alt=media&token=354c2c23-27f8-4498-8783-faf0c4952afc"));
                    suggestionList.add(new Suggestion(15,"bài tập cơ bản","12-3-2018","https://firebasestorage.googleapis.com/v0/b/demouploadvideo-bdc04.appspot.com/o/images.jpg?alt=media&token=354c2c23-27f8-4498-8783-faf0c4952afc"));
                    suggestionAdapter = new SuggestionAdapter(getApplicationContext(),R.layout.suggestion_list_item,suggestionList);
                    listView.setAdapter(suggestionAdapter);
                    isLoading = false;

                    break;
            }
            super.handleMessage(msg);
        }
    }
    public class ThreadData extends Thread{
        @Override
        public void run() {
            mHandler.sendEmptyMessage(0); // gửi 0 là vì lần đầu chay để lấy dữ liệu
            try {
                Thread.sleep(3000); // cho thằng progressbar nó ngủ 3 giây
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Message message = mHandler.obtainMessage(1); // nó là 1 phương thức liên kết thread vs hanlder
            // dùng để duy trì kết nối khi muốn gọi lại, tham số truyền vào là 1 thì nó sẽ chuyển vào cái switch case 1
            mHandler.sendMessage(message);
            super.run();
        }
    }
}
