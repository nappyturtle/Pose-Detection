package com.capstone.self_training.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.capstone.self_training.R;
import com.capstone.self_training.adapter.MainSuggestionDetailAdapter;
import com.capstone.self_training.adapter.SuggestionAdapter;
import com.capstone.self_training.fragment.SuggestionDetailFragment;
import com.capstone.self_training.model.Suggestion;
import com.capstone.self_training.model.SuggestionDetail;
import com.capstone.self_training.service.apiservice.ApiService;
import com.capstone.self_training.service.dataservice.DataService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SuggestionDetailActi extends AppCompatActivity {

    Toolbar toolbar;
    ViewPager viewPager;
    ArrayList<SuggestionDetail> suggestionDetails;
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
        ApiService apiService = DataService.getService();
        Call<List<SuggestionDetail>> callBack = apiService.getSuggestionDetail(suggestionId);
        callBack.enqueue(new Callback<List<SuggestionDetail>>() {
            @Override
            public void onResponse(Call<List<SuggestionDetail>> call, Response<List<SuggestionDetail>> response) {
                suggestionDetails = (ArrayList<SuggestionDetail>)response.body();
                MainSuggestionDetailAdapter main = new MainSuggestionDetailAdapter(getSupportFragmentManager());
                for (int i = 0; i < suggestionDetails.size(); i++) {

                    SuggestionDetailFragment fragment = SuggestionDetailFragment.newInstance(suggestionDetails.get(i));
                    main.addFragment(fragment);
                    viewPager.setAdapter(main);

                }
            }

            @Override
            public void onFailure(Call<List<SuggestionDetail>> call, Throwable t) {

            }
        });
    }

    private void reflect() {
        toolbar = (Toolbar) findViewById(R.id.suggestionDetail_toolbar_id);
        viewPager = (ViewPager) findViewById(R.id.suggestionDetail_viewPager_id);
        suggestionDetails = new ArrayList<>();
    }
}
