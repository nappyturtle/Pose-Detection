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
import android.widget.LinearLayout;
import android.widget.TextView;
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

    private Toolbar toolbar;
    private ViewPager viewPager;
    private ArrayList<SuggestionDetail> suggestionDetails;

    private SharedPreferences mPerferences;
    private SharedPreferences.Editor mEditor;
    private String token;
    private TextView txtSuggestionDetailIsEmpty;

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

        SuggestionDetailService suggestionDetailService = new SuggestionDetailService();
        suggestionDetails = (ArrayList<SuggestionDetail>) suggestionDetailService.getSuggestionDetailList(token,suggestionId);
        if(suggestionDetails.size() <= 0){
            txtSuggestionDetailIsEmpty.setVisibility(View.VISIBLE);
            viewPager.setVisibility(View.INVISIBLE);
        }else{
            txtSuggestionDetailIsEmpty.setVisibility(View.INVISIBLE);
            viewPager.setVisibility(View.VISIBLE);
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
        txtSuggestionDetailIsEmpty = (TextView)findViewById(R.id.txtSuggestionDetailIsEmpty);
        suggestionDetails = new ArrayList<>();

        mPerferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPerferences.edit();

        token = mPerferences.getString(getString(R.string.token),"");
    }
}
