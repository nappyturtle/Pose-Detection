package com.capstone.self_training.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.capstone.self_training.R;
import com.capstone.self_training.adapter.ManageTraineeTrainerAdapter;
import com.capstone.self_training.model.Account;
import com.capstone.self_training.util.CheckConnection;

import java.util.ArrayList;
import java.util.List;

public class ManageTrainerActivity extends AppCompatActivity {

    private ListView listView;
    private ManageTraineeTrainerAdapter adapter;
    private Toolbar toolbar;
    private ArrayList<Account> dtoList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_trainer);
        if(CheckConnection.haveNetworkConnection(this)){
            init();
            displayToolBar();
            getDataManagement();
            getTraineeTrainerProfile();
        }else{
            CheckConnection.showConnection(this,"Kiểm tra kết nối của bạn !!!");
        }
    }
    private void getTraineeTrainerProfile() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(),TrainerChannelActivity.class);
                //intent.putExtra("accountTemp",dtoList.get(i).getId()+"_-/-_"+dtoList.get(i).getUsername());
                intent.putExtra("accountTemp",dtoList.get(i));
                startActivity(intent);
            }
        });
    }

    // lấy danh sách trainer từ màn hình BoughtCourseActivity
    private void getDataManagement() {
        Intent intent = getIntent();
        List<Account> listTemp = (List<Account>) intent.getSerializableExtra("listManagement");
        for(Account dto : listTemp){
            dtoList.add(dto);
            adapter.notifyDataSetChanged();
        }
    }
    // hiển thị thành toolbar
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

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.manageTrainer_toolbar_id);
        listView = (ListView) findViewById(R.id.listview_manage_trainer);
        dtoList = new ArrayList<>();
        adapter = new ManageTraineeTrainerAdapter(dtoList,getApplicationContext());
        listView.setAdapter(adapter);
    }
}
