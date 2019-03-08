package com.capstone.self_training.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.capstone.self_training.R;
import com.capstone.self_training.adapter.ManageTraineeTrainerAdapter;
import com.capstone.self_training.dto.EnrollmentDTO;
import com.capstone.self_training.util.CheckConnection;

import java.util.ArrayList;
import java.util.List;

public class ManageTraineeTrainerActivity extends AppCompatActivity {

    private ListView listView;
    private ManageTraineeTrainerAdapter adapter;
    private Toolbar toolbar;
    private ArrayList<EnrollmentDTO> dtoList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_trainee_trainer);
        if(CheckConnection.haveNetworkConnection(this)){
            init();
            displayToolBar();
            getDataManagement();
        }else{
            CheckConnection.showConnection(this,"Kiểm tra kết nối của bạn !!!");
        }
    }

    private void getDataManagement() {
        Intent intent = getIntent();
        List<EnrollmentDTO> listTemp = (List<EnrollmentDTO>) intent.getSerializableExtra("listManagement");
        for(EnrollmentDTO dto : listTemp){
            dtoList.add(dto);
            adapter.notifyDataSetChanged();
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

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.manageTraineeTrainer_toolbar_id);
        listView = (ListView) findViewById(R.id.listview_manage_trainerTrainee);
        dtoList = new ArrayList<>();
        adapter = new ManageTraineeTrainerAdapter(dtoList,getApplicationContext());
        listView.setAdapter(adapter);
    }
}
