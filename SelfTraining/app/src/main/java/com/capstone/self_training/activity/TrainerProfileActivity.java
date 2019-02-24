package com.capstone.self_training.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.capstone.self_training.R;
import com.capstone.self_training.util.CheckConnection;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class TrainerProfileActivity extends AppCompatActivity {

    Toolbar toolbar;
    CircleImageView imgAccount;
    TextView txtUsername;
    ImageView imgSetting;
    LinearLayout uploadVideo;
    TextView viewSuggestion;
    TextView viewUploadedVideo;
    SharedPreferences mPerferences;
    SharedPreferences.Editor mEditor;
    int id;
    String username;
    int roleId;
    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_profile);
        if(CheckConnection.haveNetworkConnection(this)){
            reflect();
            displayToolBar();
            loadData();
            getAllSuggestion();
            getAllUploadedVideo();
            uploadVideoToStorage();
        }else{
            CheckConnection.showConnection(this,"Xin vui lòng kiểm tra kết nối internet !!! ");
            finish();
        }
    }

    private void uploadVideoToStorage() {
        uploadVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),TrainerUploadVideoActi.class);
                startActivity(intent);
            }
        });
    }

    private void getAllUploadedVideo() {
        viewUploadedVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void getAllSuggestion() {
        viewSuggestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrainerProfileActivity.this,SuggestionListActi.class);
                startActivity(intent);
            }
        });
    }


    private void reflect() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_trainer_profile_id);
        imgAccount = (CircleImageView) findViewById(R.id.img_trainer_profile_id);
        txtUsername = (TextView) findViewById(R.id.txtUsername_trainer_profile_id);
        imgSetting = (ImageView) findViewById(R.id.imgSetting_trainer_profile_id);
        uploadVideo = (LinearLayout) findViewById(R.id.lnUpload_trainer_profile_id);
        viewSuggestion = (TextView)findViewById(R.id.btnGetAllSuggestion_trainer_profile_id);
        viewUploadedVideo = (TextView) findViewById(R.id.btnGetAllVideo_trainer_profile_id);

        mPerferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPerferences.edit();
        Log.e("TrainerProfileActi: ","id = "+String.valueOf(mPerferences.getInt(getString(R.string.id),0))
        +" - username = "+mPerferences.getString(getString(R.string.username),"") + " - roleId = "+
         String.valueOf(mPerferences.getInt(getString(R.string.roleId),0)) + " - token = "+
                        mPerferences.getString(getString(R.string.token),""));

         id = mPerferences.getInt(getString(R.string.id),0);
         username = mPerferences.getString(getString(R.string.username),"");
         roleId = mPerferences.getInt(getString(R.string.roleId),0);
         token = mPerferences.getString(getString(R.string.token),"");
    }
    private void loadData(){
        txtUsername.setText(mPerferences.getString(getString(R.string.username),""));
        Picasso.get().load(mPerferences.getString(getString(R.string.imgAccount),""))
                .placeholder(R.drawable.userlogin).into(imgAccount);
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.trainer_trainee_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuChangePassword:
                Intent intentChangePassword = new Intent(getApplicationContext(),ChangePasswordActivity.class);
                startActivity(intentChangePassword);
                break;
            case R.id.menuLogout:
                SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(this);
                preferences.edit().clear().commit();
                Intent intentMain_Home = new Intent(getApplicationContext(),MainActivity_Home.class);
                startActivity(intentMain_Home);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
