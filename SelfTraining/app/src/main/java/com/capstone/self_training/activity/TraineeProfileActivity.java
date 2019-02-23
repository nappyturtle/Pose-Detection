package com.capstone.self_training.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.capstone.self_training.R;
import com.capstone.self_training.util.CheckConnection;

public class TraineeProfileActivity extends AppCompatActivity {

    Toolbar toolbar;
    ImageView imgAccount;
    TextView txtUsername;
    ImageView imgSetting;
    TextView viewSuggestion;
    SharedPreferences mPerferences;
    SharedPreferences.Editor mEditor;
    int id;
    String username;
    int roleId;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainee_profile);
        if (CheckConnection.haveNetworkConnection(this)) {
            reflect();
            displayToolBar();
            getData();
            getAllSuggestion();
        } else {
            CheckConnection.showConnection(this, "Xin vui lòng kiểm tra kết nối internet !!! ");
            finish();
        }
    }

    private void reflect() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_trainee_profile_id);
        imgAccount = (ImageView) findViewById(R.id.img_trainee_profile_id);
        txtUsername = (TextView) findViewById(R.id.txtUsername_trainee_profile_id);
        imgSetting = (ImageView) findViewById(R.id.imgSetting_trainee_profile_id);
        viewSuggestion = (TextView) findViewById(R.id.btnGetAllSuggestion_trainee_profile_id);

        mPerferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPerferences.edit();
        Log.e("TrainerProfileActi: ", "id = " + String.valueOf(mPerferences.getInt(getString(R.string.id), 0))
                + " - username = " + mPerferences.getString(getString(R.string.username), "") + " - roleId = " +
                String.valueOf(mPerferences.getInt(getString(R.string.roleId), 0)) + " - token = " +
                mPerferences.getString(getString(R.string.token), ""));

        id = mPerferences.getInt(getString(R.string.id), 0);
        username = mPerferences.getString(getString(R.string.username), "");
        roleId = mPerferences.getInt(getString(R.string.roleId), 0);
        token = mPerferences.getString(getString(R.string.token), "");
    }

    private void displayToolBar() {
    }

    private void getData() {
    }

    private void getAllSuggestion() {
        viewSuggestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TraineeProfileActivity.this, SuggestionListActi.class);
                startActivity(intent);
            }
        });
    }
}
