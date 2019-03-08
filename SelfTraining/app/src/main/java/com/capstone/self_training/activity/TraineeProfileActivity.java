package com.capstone.self_training.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
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
import android.widget.Toast;

import com.capstone.self_training.R;
import com.capstone.self_training.util.CheckConnection;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class TraineeProfileActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_UPDATE = 4;
    Toolbar toolbar;
    CircleImageView imgAccount;
    TextView txtUsername;
    ImageView imgSetting;
    TextView viewSuggestion;
    TextView viewBoughtVideo;
    SharedPreferences mPerferences;
    SharedPreferences.Editor mEditor;
    int id;
    String username;
    int roleId;
    String token;
    String imageAccount;
    LinearLayout lnSuggestion;
    LinearLayout lnBoughtCourse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainee_profile);
        if(CheckConnection.haveNetworkConnection(this)){
            reflect();
            displayToolBar();
            loadData();
            getAllSuggestion();
            getProfileTrainee();
            getAllBoughtCourse();
        }else{
            CheckConnection.showConnection(this,"Xin vui lòng kiểm tra kết nối internet !!! ");
            finish();
        }
    }
    private void getProfileTrainee(){
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

    private void reflect() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_trainee_profile_id);
        imgAccount = (CircleImageView) findViewById(R.id.img_trainee_profile_id);
        txtUsername = (TextView) findViewById(R.id.txtUsername_trainee_profile_id);
        imgSetting = (ImageView) findViewById(R.id.imgSetting_trainee_profile_id);
        viewSuggestion = (TextView)findViewById(R.id.btnGetAllSuggestion_trainee_profile_id);
        viewBoughtVideo = (TextView)findViewById(R.id.btnGetAllBoughtVideo_trainee_profile_id);
        lnSuggestion = (LinearLayout) findViewById(R.id.ln_traineeProfile_getAllSuggestion);
        lnBoughtCourse = (LinearLayout) findViewById(R.id.ln_traineeProfile_getAllBoughtCourse);

        mPerferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPerferences.edit();
        Log.e("TraineeProfileActi: ","id = "+String.valueOf(mPerferences.getInt(getString(R.string.id),0))
                +" - username = "+mPerferences.getString(getString(R.string.username),"") + " - roleId = "+
                String.valueOf(mPerferences.getInt(getString(R.string.roleId),0)) + " - token = "+
                mPerferences.getString(getString(R.string.token),""));

        id = mPerferences.getInt(getString(R.string.id),0);
        username = mPerferences.getString(getString(R.string.username),"");
        roleId = mPerferences.getInt(getString(R.string.roleId),0);
        token = mPerferences.getString(getString(R.string.token),"");
        imageAccount = mPerferences.getString(getString(R.string.imgAccount),"");
        //Log.e("traineeImage = ",imageAccount);
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
    private void loadData() {
        txtUsername.setText(mPerferences.getString(getString(R.string.username),""));
        Picasso.get().load(imageAccount)
//                .placeholder(R.drawable.userlogin).into(imgAccount);
        .into(imgAccount);
    }
    private void getAllSuggestion() {
        viewSuggestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TraineeProfileActivity.this,SuggestionListActi.class);
                startActivity(intent);
            }
        });
        lnSuggestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TraineeProfileActivity.this,SuggestionListActi.class);
                startActivity(intent);
            }
        });
    }
    private void getAllBoughtCourse() {
        viewBoughtVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TraineeProfileActivity.this,BoughtCourseActivity.class);
                startActivity(intent);
            }
        });
        lnBoughtCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TraineeProfileActivity.this,BoughtCourseActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_UPDATE && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            System.out.println("Trainee uploaded profile");
            imageAccount = data.getStringExtra("imgAccount");

        }
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
                confirmLogout();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void confirmLogout() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Đăng xuất");

        alertDialog.setMessage("Bạn có muốn đăng xuất không?");
        alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                preferences.edit().clear().commit();
                Intent intentMain_Home = new Intent(getApplicationContext(),MainActivity_Home.class);
                startActivity(intentMain_Home);
                Toast.makeText(TraineeProfileActivity.this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        dialog.dismiss();
                    }
                });
            }
        });
        alertDialog.show();
    }
}
