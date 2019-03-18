package com.capstone.self_training.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.capstone.self_training.R;
import com.capstone.self_training.adapter.SuggestionDetailByTrainerAdapter;
import com.capstone.self_training.model.SuggestionDetail;
import com.capstone.self_training.service.dataservice.SuggestionDetailService;

import java.util.ArrayList;
import java.util.List;

public class SuggestionDetailListByTrainerActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerViewSuggestionDetail;
    private List<SuggestionDetail> suggestionDetails;
    private SuggestionDetailByTrainerAdapter adapter;
    private SharedPreferences mPerferences;
    private String token;
    private TextView txtSuggestionDetailIsEmpty;
    private int suggestionId;
    public static final int REQUEST_CODE_SAVE_COMMENT = 0x123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion_detail_list_by_trainer);

        reflect();

        Intent intent = getIntent();
        suggestionId = intent.getIntExtra("suggestionId", 1);
        //init();
        getData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_SAVE_COMMENT && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
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

    // hiển thị suggestion detail
    private void getData() {


        SuggestionDetailService suggestionDetailService = new SuggestionDetailService();
        List<SuggestionDetail> suggestionDetailsTemp = suggestionDetailService.getSuggestionDetailList(token, suggestionId);
        for (SuggestionDetail suggestionDetail : suggestionDetailsTemp) {
            suggestionDetails.add(suggestionDetail);
            adapter.notifyDataSetChanged();
        }

        Log.e("da vao lan nua", "da vao lan nua");
        if (suggestionDetails.size() <= 0) {
            txtSuggestionDetailIsEmpty.setVisibility(View.VISIBLE);
            recyclerViewSuggestionDetail.setVisibility(View.INVISIBLE);
        } else {
            txtSuggestionDetailIsEmpty.setVisibility(View.INVISIBLE);
            recyclerViewSuggestionDetail.setVisibility(View.VISIBLE);
        }
    }

    private void reflect() {
        toolbar = (Toolbar) findViewById(R.id.suggestionDetail_toolbar_id_byTrainer);
        txtSuggestionDetailIsEmpty = (TextView) findViewById(R.id.txtSuggestionDetailIsEmptyByTrainer);

        if (suggestionDetails == null)
            suggestionDetails = new ArrayList<>();
        recyclerViewSuggestionDetail = (RecyclerView) findViewById(R.id.suggestionDetail_recycleview_id_byTrainer);
        recyclerViewSuggestionDetail.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new SuggestionDetailByTrainerAdapter(suggestionDetails, SuggestionDetailListByTrainerActivity.this);
        recyclerViewSuggestionDetail.setHasFixedSize(true);
        recyclerViewSuggestionDetail.setAdapter(adapter);

        mPerferences = PreferenceManager.getDefaultSharedPreferences(this);

        token = mPerferences.getString(getString(R.string.token), "");
        displayToolBar();
    }
}

