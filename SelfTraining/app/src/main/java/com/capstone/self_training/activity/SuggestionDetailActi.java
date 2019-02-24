package com.capstone.self_training.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.capstone.self_training.R;
import com.capstone.self_training.adapter.MainSuggestionDetailAdapter;
import com.capstone.self_training.fragment.SuggestionDetailFragment;
import com.capstone.self_training.model.SuggestionDetail;
import com.capstone.self_training.service.dataservice.SuggestionDetailService;
import com.capstone.self_training.service.iService.ISuggestionDetailService;
import com.capstone.self_training.service.dataservice.DataService;
import com.capstone.self_training.util.Constants;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SuggestionDetailActi extends AppCompatActivity {

    Toolbar toolbar;
    ViewPager viewPager;
    ArrayList<SuggestionDetail> suggestionDetails;

    SharedPreferences mPerferences;
    SharedPreferences.Editor mEditor;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion_detail);
        reflect();
        displayToolBar();
        //init();
        getData();
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

    private void getData(){
        Intent intent = getIntent();
        int suggestionId = intent.getIntExtra("suggestionId",1);
//        ISuggestionDetailService suggestionDetailService = DataService.getSuggestionDetailService();
////        Call<List<SuggestionDetail>> callBack = suggestionDetailService.getSuggestionDetail(token,suggestionId);
////        callBack.enqueue(new Callback<List<SuggestionDetail>>() {
////            @Override
////            public void onResponse(Call<List<SuggestionDetail>> call, Response<List<SuggestionDetail>> response) {
////                if(response.code() == Constants.Status_Ok) {
////                    suggestionDetails = (ArrayList<SuggestionDetail>) response.body();
////                    if(suggestionDetails.size() == 0){
////                        // hiển TextView
////                    }else {
////                        MainSuggestionDetailAdapter main = new MainSuggestionDetailAdapter(getSupportFragmentManager());
////                        for (int i = 0; i < suggestionDetails.size(); i++) {
////                            SuggestionDetailFragment fragment = SuggestionDetailFragment.newInstance(suggestionDetails.get(i));
////                            main.addFragment(fragment);
////                            viewPager.setAdapter(main);
////                        }
////                    }
////                }else{
////                    Toast.makeText(SuggestionDetailActi.this, "Hệ thống bị đang bị lỗi, Vui lòng quay lại sau", Toast.LENGTH_SHORT).show();
////                }
////            }
////
////            @Override
////            public void onFailure(Call<List<SuggestionDetail>> call, Throwable t) {
////                Toast.makeText(SuggestionDetailActi.this, "Hệ thống bị đang bị lỗi "+t.getMessage(), Toast.LENGTH_SHORT).show();
////                Log.e("SuggestionListActi onFailure: ",t.getMessage());
////            }
////        });
        SuggestionDetailService suggestionDetailService = new SuggestionDetailService();
        suggestionDetails = (ArrayList<SuggestionDetail>) suggestionDetailService.getSuggestionDetailList(token,suggestionId);
        if(suggestionDetails.size() == 0){

        }else{
            MainSuggestionDetailAdapter main = new MainSuggestionDetailAdapter(getSupportFragmentManager());
            for (int i = 0; i < suggestionDetails.size(); i++) {
                SuggestionDetailFragment fragment = SuggestionDetailFragment.newInstance(suggestionDetails.get(i));
                main.addFragment(fragment);
                viewPager.setAdapter(main);
            }
        }
    }

    private void reflect() {
        toolbar = (Toolbar) findViewById(R.id.suggestionDetail_toolbar_id);
        viewPager = (ViewPager) findViewById(R.id.suggestionDetail_viewPager_id);
        suggestionDetails = new ArrayList<>();

        mPerferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPerferences.edit();

        token = mPerferences.getString(getString(R.string.token),"");
    }
}
