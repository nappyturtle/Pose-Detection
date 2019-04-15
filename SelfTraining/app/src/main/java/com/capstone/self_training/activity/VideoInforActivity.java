package com.capstone.self_training.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.capstone.self_training.R;
import com.capstone.self_training.adapter.CategoryAdapter;
import com.capstone.self_training.adapter.CourseByNameAdapter;
import com.capstone.self_training.dto.CourseDTO;
import com.capstone.self_training.helper.TimeHelper;
import com.capstone.self_training.model.Account;
import com.capstone.self_training.model.Category;
import com.capstone.self_training.model.Course;
import com.capstone.self_training.model.Video;
import com.capstone.self_training.service.dataservice.CategoryService;
import com.capstone.self_training.service.dataservice.CourseService;
import com.capstone.self_training.service.dataservice.VideoService;
import com.capstone.self_training.util.Constants;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class VideoInforActivity extends AppCompatActivity {

    private TextInputEditText edtEditVideoTitle;
    private ImageView ivVideoThumbnail;
    private Spinner spnStatus;
    //private CourseService courseService;
    private Button btnSave;
    //private List<Course> courseList;
    //List<Course> courses;
    //CourseByNameAdapter courseByNameAdapter;
    private Video videoIntent;
    public String statusSelected;
    private SharedPreferences mPerferences;
    private static int PICK_IMAGE_REQUEST = 1;
    private Uri videoThumbnailUri;
    private boolean isChooseThumbnail = false;
    private Toolbar toolbar;
    private StorageReference storageReference;
    private String token;
    private int accountId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_info);

        Intent intent = getIntent();
        videoIntent = (Video) intent.getSerializableExtra("EDIT_VIDEO");
        init();
        setupToolbar();

        getDataFromIntent();
        initStatusListAdapter();
        openFileChooser();
        clickToEditVideoButton();


    }


    private void getDataFromIntent() {

        edtEditVideoTitle.setText(videoIntent.getTitle());
        Picasso.get().load(videoIntent.getThumnailUrl()).into(ivVideoThumbnail);
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_edit_video_by_trainer);
        ivVideoThumbnail = (ImageView) findViewById(R.id.ivEditVideoThumbnail);
        edtEditVideoTitle = (TextInputEditText) findViewById(R.id.edtEditVideoTitle);
        spnStatus = (Spinner) findViewById(R.id.spnEditVideoStatus);
        btnSave = (Button) findViewById(R.id.btnEditVideo);
        mPerferences = PreferenceManager.getDefaultSharedPreferences(this);
        storageReference = FirebaseStorage.getInstance().getReference();
        token = mPerferences.getString(getString(R.string.token), "");
        accountId = mPerferences.getInt(getString(R.string.id), 0);

    }


    private void initStatusListAdapter() {

        List<String> dataSrc = new ArrayList<String>();

        if(videoIntent.getStatus().equals("active")){
            dataSrc.add("Đang hoạt động");
            dataSrc.add("Ngừng hoạt động");
        }else{
            dataSrc.add("Ngừng hoạt động");
            dataSrc.add("Đang hoạt động");
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dataSrc);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnStatus.setAdapter(arrayAdapter);
        spnStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                statusSelected =parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                statusSelected = "not_selected";

            }
        });

    }

    private void openFileChooser() {
        ivVideoThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            videoThumbnailUri = data.getData();
            Picasso.get().load(videoThumbnailUri).into(ivVideoThumbnail);
            isChooseThumbnail = true;
        }
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setTitle(videoIntent.getTitle());
    }

    private void clickToEditVideoButton() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateVideo()){
                    confirmEditVideo();
                }else{
                    Toast.makeText(VideoInforActivity.this, "Xin vui lòng nhập những thông tin cần thiết", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void editVideo() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Đang xử lý");
        progressDialog.show();
        if (!isChooseThumbnail) {
            Video video = new Video();

            video.setId(videoIntent.getId());
            video.setUpdatedTime(TimeHelper.getCurrentTime());
            video.setTitle(edtEditVideoTitle.getText().toString());
            video.setThumnailUrl(videoIntent.getThumnailUrl());
            if (statusSelected.equals("Đang hoạt động")) {
                statusSelected = "active";
            } else if (statusSelected.equals("Ngừng hoạt động")) {
                statusSelected = "inactive";
            }
            Log.e("statusSelected = ",statusSelected);
            video.setStatus(statusSelected);


            VideoService videoService = new VideoService();
            if (videoService.editVideo(token, video)) {
                Toast.makeText(VideoInforActivity.this, "Thay đổi thông tin video thành công", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                Intent intent = new Intent();
                setResult(Activity.RESULT_OK, intent);
                finish();
            } else {
                progressDialog.dismiss();
                Toast.makeText(VideoInforActivity.this, "Thay đổi thông tin video không thành công", Toast.LENGTH_SHORT).show();
            }


        } else {
            StorageReference srf = storageReference.child(videoIntent.getFolderName() + "/" + edtEditVideoTitle.getText().toString().trim() + "-thumbnail");
            srf.putFile(videoThumbnailUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Video video = new Video();

                    video.setId(videoIntent.getId());
                    video.setUpdatedTime(TimeHelper.getCurrentTime());
                    video.setTitle(edtEditVideoTitle.getText().toString());
                    video.setThumnailUrl(taskSnapshot.getDownloadUrl().toString());
                    if (statusSelected.equals("Đang hoạt động")) {
                        statusSelected = "active";
                    } else if (statusSelected.equals("Ngừng hoạt động")) {
                        statusSelected = "inactive";
                    }
                    Log.e("statusSelected = ",statusSelected);
                    video.setStatus(statusSelected);

                    VideoService videoService = new VideoService();
                    if (videoService.editVideo(token, video)) {
                        Toast.makeText(VideoInforActivity.this, "Thay đổi thông tin video thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    } else {
                        Toast.makeText(VideoInforActivity.this, "Thay đổi thông tin video không thành công", Toast.LENGTH_SHORT).show();
                    }

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    progressDialog.setMessage("Hoàn thành " + (int) progress + "%...");
                    if ((int) progress == 100) {
                        progressDialog.dismiss();
                    }
                }
            });
        }
    }


    public void confirmEditVideo() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Thay đổi thông tin ");

        alertDialog.setMessage("Bạn có muốn thay đổi thông khóa học này ?");
        alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editVideo();

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

    private boolean validateVideo() {
        if (edtEditVideoTitle.getText().toString().equals("") || edtEditVideoTitle.getText().toString() == null) {
            Toast.makeText(this, "Bạn chưa điền tên khóa học!", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

}