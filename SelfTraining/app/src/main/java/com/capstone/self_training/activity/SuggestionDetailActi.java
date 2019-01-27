package com.capstone.self_training.activity;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.capstone.self_training.R;
import com.capstone.self_training.adapter.MainSuggestionDetailAdapter;
import com.capstone.self_training.fragment.SuggestionDetailFragment;
import com.capstone.self_training.model.SuggestionDetail;

import java.util.ArrayList;

public class SuggestionDetailActi extends AppCompatActivity {

    Toolbar toolbar;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion_detail);
        reflect();
        displayToolBar();
        init();
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
        MainSuggestionDetailAdapter main = new MainSuggestionDetailAdapter(getSupportFragmentManager());
        ArrayList<SuggestionDetail> list = new ArrayList<>();
        list.add(new SuggestionDetail(1,"https://firebasestorage.googleapis.com/v0/b/demouploadvideo-bdc04.appspot.com/o/images.jpg?alt=media&token=354c2c23-27f8-4498-8783-faf0c4952afc",
                "https://firebasestorage.googleapis.com/v0/b/demouploadvideo-bdc04.appspot.com/o/images%20(2).jpg?alt=media&token=00f422f6-36bc-44d8-8b75-85986d92dee7",
                "Hình của bạn chỉ đúng 20%"));
        list.add(new SuggestionDetail(2,"https://firebasestorage.googleapis.com/v0/b/demouploadvideo-bdc04.appspot.com/o/download.jpg?alt=media&token=f003f004-b713-4cf4-91ea-6894e13bdaac",
                "https://firebasestorage.googleapis.com/v0/b/demouploadvideo-bdc04.appspot.com/o/images.jpg?alt=media&token=354c2c23-27f8-4498-8783-faf0c4952afc",
                "Hình của bạn chỉ đúng 30%"));
        list.add(new SuggestionDetail(3,"https://firebasestorage.googleapis.com/v0/b/demouploadvideo-bdc04.appspot.com/o/images.jpg?alt=media&token=354c2c23-27f8-4498-8783-faf0c4952afc",
                "https://firebasestorage.googleapis.com/v0/b/demouploadvideo-bdc04.appspot.com/o/images%20(1).jpg?alt=media&token=b270aaef-6cf2-4a08-8ce9-40ef57734a19",
                "Hình của bạn chỉ đúng 40%"));
        list.add(new SuggestionDetail(4,"https://firebasestorage.googleapis.com/v0/b/demouploadvideo-bdc04.appspot.com/o/download12.jpg?alt=media&token=3bb18243-9aea-4521-8a28-99202d404177",
                "https://firebasestorage.googleapis.com/v0/b/demouploadvideo-bdc04.appspot.com/o/images.jpg?alt=media&token=354c2c23-27f8-4498-8783-faf0c4952afc",
                "Hình của bạn chỉ đúng 50%"));

        for (int i = 0; i < list.size(); i++) {

        SuggestionDetailFragment fragment = SuggestionDetailFragment.newInstance(list.get(i));
            main.addFragment(fragment);
            viewPager.setAdapter(main);

        }
    }

    private void reflect() {
        toolbar = (Toolbar) findViewById(R.id.suggestionDetail_toolbar_id);
        viewPager = (ViewPager) findViewById(R.id.suggestionDetail_viewPager_id);
    }
}
