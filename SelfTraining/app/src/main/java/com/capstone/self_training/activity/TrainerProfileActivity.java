package com.capstone.self_training.activity;

import android.app.Activity;
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

    private static final int REQUEST_CODE_UPDATE = 4;
    Toolbar toolbar;
    CircleImageView imgAccount;
    TextView txtUsername;
    ImageView imgSetting;
    LinearLayout uploadVideo;
    TextView viewSuggestion;
    TextView viewUploadedVideo;
    TextView viewAllBoughtCourse;
    SharedPreferences mPerferences;
    SharedPreferences.Editor mEditor;
    int id;
    String username;
    int roleId;
    String token;
    String imageAccount;
    LinearLayout lnSuggestion;
    LinearLayout lnBoughtCourse;
    LinearLayout lnUploadedVideo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_profile);
        if (CheckConnection.haveNetworkConnection(this)) {
            reflect();
            displayToolBar();
            loadData();
            getAllSuggestion();
            getAllUploadedVideo();
            uploadVideoToStorage();
            getAllBoughtVideo();
        } else {
            CheckConnection.showConnection(this, "Xin vui lòng kiểm tra kết nối internet !!! ");
            finish();
        }
    }

    private void getAllBoughtVideo() {
        viewAllBoughtCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),BoughtCourseActivity.class);
                startActivity(intent);
            }
        });
        lnBoughtCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),BoughtCourseActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_UPDATE && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            System.out.println("Trainee uploaded profile");
            imageAccount = data.getStringExtra("imgAccount");
            Log.e("imageAccount TrainerProfileActivity = ",imageAccount);
        }
    }

    private void getProfileTrainer(){
        imgSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                startActivityForResult(intent, REQUEST_CODE_LOGIN);
                Intent intent = new Intent(getApplicationContext(), UpdateProfileActivity.class);
                intent.putExtra("USER_ID", id);
                startActivityForResult(intent, REQUEST_CODE_UPDATE);
                //startActivity(intent);
            }
        });
    }
    private void uploadVideoToStorage() {
        uploadVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TrainerUploadVideoActi.class);
                startActivity(intent);
            }
        });
    }

    private void getAllUploadedVideo() {
        viewUploadedVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrainerProfileActivity.this, TrainerVideoListActivity.class);
                startActivity(intent);
            }
        });
        lnUploadedVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrainerProfileActivity.this, TrainerVideoListActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getAllSuggestion() {
        viewSuggestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrainerProfileActivity.this, SuggestionListActi.class);
                startActivity(intent);
            }
        });
        lnSuggestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrainerProfileActivity.this, SuggestionListActi.class);
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
        viewSuggestion = (TextView) findViewById(R.id.btnGetAllSuggestion_trainer_profile_id);
        viewUploadedVideo = (TextView) findViewById(R.id.btnGetAllVideo_trainer_profile_id);
        viewAllBoughtCourse = (TextView) findViewById(R.id.btnGetAllBoughtVideo_trainer_profile_id);
        lnSuggestion = (LinearLayout) findViewById(R.id.ln_trainerProfile_getAllSuggestion_id);
        lnBoughtCourse = (LinearLayout) findViewById(R.id.ln_trainerProfile_getAllCourse_id);
        lnUploadedVideo = (LinearLayout) findViewById(R.id.ln_trainerProfile_getAllUploadedVideo_id);

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

    private void loadData() {
        txtUsername.setText(mPerferences.getString(getString(R.string.username), ""));
        Picasso.get().load(mPerferences.getString(getString(R.string.imgAccount), ""))
                .placeholder(R.drawable.userlogin).into(imgAccount);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.trainer_trainee_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuChangePassword:
                Intent intentChangePassword = new Intent(getApplicationContext(), ChangePasswordActivity.class);
                startActivity(intentChangePassword);
                break;
            case R.id.menuLogout:
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                preferences.edit().clear().commit();
                Intent intentMain_Home = new Intent(getApplicationContext(), MainActivity_Home.class);
                startActivity(intentMain_Home);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public void updateProfile(View view) {
        Intent intent = new Intent(getApplicationContext(), UpdateProfileActivity.class);
        intent.putExtra("USER_ID", id);
        startActivityForResult(intent, REQUEST_CODE_UPDATE);

        //startActivity(intent);
    }

}
