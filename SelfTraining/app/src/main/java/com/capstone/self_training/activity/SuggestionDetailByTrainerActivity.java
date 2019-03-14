package com.capstone.self_training.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.capstone.self_training.R;
import com.capstone.self_training.adapter.MainSuggestionDetailAdapter;
import com.capstone.self_training.adapter.MainSuggestionDetailByTrainerAdapter;
import com.capstone.self_training.fragment.SuggestionDetailFragment;
import com.capstone.self_training.fragment.SuggestionDetailFragmentByTrainer;
import com.capstone.self_training.model.SuggestionDetail;
import com.capstone.self_training.service.dataservice.SuggestionDetailService;

import java.util.ArrayList;

public class SuggestionDetailByTrainerActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_suggestion_detail_by_trainer);
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

    // hiển thị suggestion detail
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
            // đưa là list suggestion detail, dùng 1 adapter để quản lí, truyền list vào đó,
            // MainSuggestionDetailAdapter có method add 1 suggestion detail vào, để tao ra 1 fragment
            MainSuggestionDetailByTrainerAdapter main = new MainSuggestionDetailByTrainerAdapter(getSupportFragmentManager());
            for (int i = 0; i < suggestionDetails.size(); i++) {
                // để truyền 1 object vào trong fragment thì sẽ khởi tạo 1 constructor newInstance
                SuggestionDetailFragmentByTrainer fragment = SuggestionDetailFragmentByTrainer.newInstance(suggestionDetails.get(i));
                main.addFragment(fragment);
                viewPager.setAdapter(main);
            }
        }
    }

    private void reflect() {
        toolbar = (Toolbar) findViewById(R.id.suggestionDetail_toolbar_id_byTrainer);
        viewPager = (ViewPager) findViewById(R.id.suggestionDetail_viewPager_id_byTrainer);
        txtSuggestionDetailIsEmpty = (TextView)findViewById(R.id.txtSuggestionDetailIsEmptyByTrainer);
        suggestionDetails = new ArrayList<>();

        mPerferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPerferences.edit();

        token = mPerferences.getString(getString(R.string.token),"");
    }
}

