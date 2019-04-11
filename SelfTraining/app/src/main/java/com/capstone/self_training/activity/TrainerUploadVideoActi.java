package com.capstone.self_training.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.capstone.self_training.R;
import com.capstone.self_training.adapter.CategoryAdapter;
import com.capstone.self_training.adapter.CourseByNameAdapter;
import com.capstone.self_training.helper.TimeHelper;
import com.capstone.self_training.model.Category;
import com.capstone.self_training.model.Course;
import com.capstone.self_training.model.Video;
import com.capstone.self_training.service.dataservice.CategoryService;
import com.capstone.self_training.service.dataservice.CourseService;
import com.capstone.self_training.service.dataservice.VideoService;
import com.capstone.self_training.util.MP4Demuxer;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jcodec.common.DemuxerTrack;
import org.jcodec.common.io.NIOUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;

public class TrainerUploadVideoActi extends AppCompatActivity {

    private static int PICK_IMAGE_REQUEST = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private static final int SELECT_VIDEO = 3;

    private VideoView vwTrainerVideo;
    private Button btnTrainerChooseVideo;
    private Button btnTrainerUpVideo;
    private EditText edtTitle;
    private Toolbar toolbar;
    private Spinner spnCourse;
    private ImageView ivAddNewCourse;

    private StorageReference storageReference;

    private Uri selectedVideoUri;
    private String selectedPath;
    private String filename;

    private ImageView ivThumbnail;
    private Uri imageUri;
    Bitmap bitmap_thumbnail;
    private ArrayList<Category> cateList;
    private CategoryAdapter categoryAdapter;
    private CategoryService categoryService;
    private SharedPreferences mPerferences;
    private SharedPreferences.Editor mEditor;
    private boolean checkedThumnail;
    private CourseService courseService;
    private int currentUserId;
    private ArrayList<Course> courseArrayList;
    private CourseByNameAdapter courseByNameAdapter;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_upload_video);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        init();
        //getSeekbarResult();
        requestRead();
        uploadVideo();

        ivThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    public void init() {

        ivThumbnail = (ImageView) findViewById(R.id.ivThumbnail);
        vwTrainerVideo = (VideoView) findViewById(R.id.vwTrainerVideo);
        btnTrainerChooseVideo = (Button) findViewById(R.id.btnTrainerChooseVideo);
        btnTrainerUpVideo = (Button) findViewById(R.id.btnTrainerUpVideo);
        edtTitle = (EditText) findViewById(R.id.edtTitle);
        storageReference = FirebaseStorage.getInstance().getReference();

        toolbar = (Toolbar) findViewById(R.id.trainer_upload_toolbar_id);
        setupToolbar();

        mPerferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPerferences.edit();

        token = mPerferences.getString(getString(R.string.token), "");
        currentUserId = mPerferences.getInt(getString(R.string.id), 0);

        storageReference = FirebaseStorage.getInstance().getReference();

        courseService = new CourseService();
        spnCourse = findViewById(R.id.spnCourse);
        initCourseList();
        courseByNameAdapter = new CourseByNameAdapter(this, courseArrayList);
        spnCourse.setAdapter(courseByNameAdapter);

        ivAddNewCourse = findViewById(R.id.ivAddCourse);
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
    }

    private void initCateList() {
        categoryService = new CategoryService();
        List<Category> arrCate = categoryService.getAllcategories();
        if (cateList == null) {
            cateList = new ArrayList<Category>();
        }
        if (arrCate != null) {
            for (Category c : arrCate) {
                cateList.add(c);
            }
        }
    }

    private void initCourseList() {
        List<Course> courses = courseService.getAllCoursesByAccountId(token, currentUserId);
        if (courseArrayList == null) {
            courseArrayList = new ArrayList<>();
        }
        if (courses != null) {
            for (Course c : courses) {
                courseArrayList.add(c);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();
            Picasso.get().load(imageUri).into(ivThumbnail);
            checkedThumnail = true;
        }

        if (requestCode == SELECT_VIDEO && resultCode == RESULT_OK && data != null && data.getData() != null) {
            System.out.println("Select video  ");
            selectedVideoUri = data.getData();
            selectedPath = getPath(selectedVideoUri);
            filename = selectedPath.substring(selectedPath.lastIndexOf("/") + 1);
            Uri uri = Uri.parse(selectedPath);
            vwTrainerVideo.setVideoURI(uri);
            MediaController mediaController = new MediaController(this);
            vwTrainerVideo.setMediaController(mediaController);
            mediaController.setAnchorView(vwTrainerVideo);

            bitmap_thumbnail = ThumbnailUtils.createVideoThumbnail(getPath(selectedVideoUri),
                    MediaStore.Images.Thumbnails.MINI_KIND);
            ivThumbnail.setImageBitmap(bitmap_thumbnail);
            checkedThumnail = false;
            imageUri = null;
        }
    }

    private void uploadVideo() {

        btnTrainerUpVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = true;
                if (edtTitle.getText().toString() == null || edtTitle.getText().toString().equals("")
                        || selectedVideoUri == null || selectedVideoUri.equals("")) {
                    checked = false;
                    Toast.makeText(TrainerUploadVideoActi.this, "Xin vui lòng điền đầy đủ thông tin video trước khi đăng", Toast.LENGTH_SHORT).show();
                }
                if (checked) {
                    Toast.makeText(TrainerUploadVideoActi.this, "da vao upload video", Toast.LENGTH_SHORT).show();
                    //checkVideoLength();
                    confirmUploadingVideo();
                }
            }
        });
    }

    private void chooseFileUpload() {

        btnTrainerChooseVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChooseVideoView();
            }
        });

    }

    public void openChooseVideoView() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select a Video "), SELECT_VIDEO);
    }

//    private void checkVideoLength() {
//        double frameRate = 0;
//        try {
//            SeekableByteChannel bc = (SeekableByteChannel) NIOUtils.readableFileChannel(String.valueOf(selectedPath));
//            DemuxerTrack vt;
//            try (MP4Demuxer dm = new MP4Demuxer((org.jcodec.common.io.SeekableByteChannel) bc)) {
//                vt = dm.getVideoTrack();
//            }
//            frameRate = vt.getMeta().getTotalDuration();
//            if (frameRate > 30) {
//                Toast.makeText(this, "Please input again ", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(this, "Okkkkk ", Toast.LENGTH_SHORT).show();
//            }
//            System.out.println("frame rate ===== " + frameRate);
//        } catch (Exception e) {
//
//        }
//    }

    private String createFolderName(String username) {
        return username + "-" + System.currentTimeMillis();
    }

    private void uploadFileToFirebase() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading");
        progressDialog.show();

        String username = mPerferences.getString(getString(R.string.username), "");

        final String foldername = createFolderName(username);
        final StorageReference stR = storageReference.child(foldername + "/" + edtTitle.getText().toString());

        stR.putFile(selectedVideoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                final Video videoUploadedToFirebase = new Video();
                //videoUploadedToFirebase.setAccountId(mPerferences.getInt(getString(R.string.id), 0));
                videoUploadedToFirebase.setNumOfView(0);
                videoUploadedToFirebase.setFolderName(foldername);
                videoUploadedToFirebase.setStatus("active");
                videoUploadedToFirebase.setPrevStatus("active");
                videoUploadedToFirebase.setTitle(edtTitle.getText().toString());
                videoUploadedToFirebase.setContentUrl(taskSnapshot.getDownloadUrl().toString());
                videoUploadedToFirebase.setCreatedTime(TimeHelper.getCurrentTime());
                Course seletedCourse = (Course) spnCourse.getSelectedItem();
                videoUploadedToFirebase.setCourseId(seletedCourse.getId());

                // add thumbnail được chọn trong bộ sưu tập
                if (checkedThumnail && imageUri != null && !imageUri.equals("")) {
                    Log.e("thumbnail bo suu tập = ", "đã vào bộ suu tập");
                    StorageReference srImg = storageReference.child(foldername + "/" + edtTitle.getText().toString() + "-thumbnail");
                    srImg.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            videoUploadedToFirebase.setThumnailUrl(taskSnapshot.getDownloadUrl().toString());

                            VideoService videoService = new VideoService();
                            videoService.createVideo(mPerferences.getString(getString(R.string.token), ""),
                                    videoUploadedToFirebase);

                            Intent intent = new Intent(TrainerUploadVideoActi.this, MainActivity_Home.class);
                            startActivity(intent);
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (50.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("Hoàn thành " + (int) progress + "%...");
                            if ((int) progress == 50) {
                                progressDialog.dismiss();
                            }
                        }
                    });
                }

                Toast.makeText(TrainerUploadVideoActi.this, "Đăng video thành công", Toast.LENGTH_LONG).show();

                // add thumbnail mặc định từ video
                if (!checkedThumnail) {
                    Log.e("thumbnail default = ", "đã vào thumbnail default");
                    StorageReference thumbnailDefault = storageReference.child(foldername + "/" + edtTitle.getText().toString() + "- video thumbnail");
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap_thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data = baos.toByteArray();
                    thumbnailDefault.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            videoUploadedToFirebase.setThumnailUrl(taskSnapshot.getDownloadUrl().toString());

                            VideoService videoService = new VideoService();

                            videoService.createVideo(mPerferences.getString(getString(R.string.token), ""),
                                    videoUploadedToFirebase);
                            Toast.makeText(TrainerUploadVideoActi.this, "upload image success", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(TrainerUploadVideoActi.this, MainActivity_Home.class);
                            startActivity(intent);
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
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(TrainerUploadVideoActi
                        .this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                progressDialog.setMessage("Hoàn thành " + (int) progress + "%...");
//                if ((int) progress == 100) {
//                    progressDialog.dismiss();
//                }
            }
        });

    }

    public void confirmUploadingVideo() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Đăng video");

        alertDialog.setMessage("Bạn có muốn đăng video này không ?");
        alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                uploadFileToFirebase();
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


    /**
     * requestPermissions and do something
     */
    public void requestRead() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            chooseFileUpload();
        }
    }

    /**
     * onRequestPermissionsResult
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                chooseFileUpload();
            } else {
                // Permission Denied
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();
        cursor = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null,
                MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
        cursor.close();
        return path;
    }

    public void addNewCourse(View view) {
        Intent intent = new Intent(this, CreateCourseActivity.class);
        startActivity(intent);
    }
}
