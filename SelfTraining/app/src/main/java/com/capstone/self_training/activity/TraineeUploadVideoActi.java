package com.capstone.self_training.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.capstone.self_training.R;
import com.capstone.self_training.model.Suggestion;
import com.capstone.self_training.model.Video;
import com.capstone.self_training.service.dataservice.SuggestionService;
import com.capstone.self_training.service.dataservice.VideoService;
import com.capstone.self_training.util.Constants;
import com.capstone.self_training.util.MP4Demuxer;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jcodec.common.DemuxerTrack;
import org.jcodec.common.io.NIOUtils;

import java.nio.channels.SeekableByteChannel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TraineeUploadVideoActi extends AppCompatActivity {
    private static final int SELECT_VIDEO = 3;
    private Button btnChooseFile;
    private Button btnUploadVideo;
    private TextView txtVideoName;
    private VideoView videoView;

    private StorageReference storageReference;
    private DatabaseReference mDatabase;

    private Uri selectedVideoUri;
    private String selectedPath;
    private String filename;

    //private VideoService videoService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_video);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        //openChooseVideoView();

        init();

        requestRead();

        uploadVideo();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_VIDEO && resultCode == RESULT_OK && data != null && data.getData() != null) {
            System.out.println("Select video  ");
            selectedVideoUri = data.getData();
            selectedPath = getPath(selectedVideoUri);
            filename = selectedPath.substring(selectedPath.lastIndexOf("/") + 1);
            txtVideoName.setText(filename);
            Uri uri = Uri.parse(selectedPath);
            videoView.setVideoURI(uri);
            MediaController mediaController = new MediaController(this);
            videoView.setMediaController(mediaController);
            mediaController.setAnchorView(videoView);
        }
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

    public void init() {
        btnChooseFile = (Button) findViewById(R.id.btnChooseFile);
        btnUploadVideo = (Button) findViewById(R.id.btnUploadVideo);
        txtVideoName = (TextView) findViewById(R.id.txtVideoName);
        videoView = (VideoView) findViewById(R.id.vwVideo);

        storageReference = FirebaseStorage.getInstance().getReference();
        //mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS);

    }

    public void openChooseVideoView() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select a Video "), SELECT_VIDEO);
    }

    private void chooseFileUpload() {

        btnChooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TraineeUploadVideoActi.this, "aaaaaaaaaaaaaaa", Toast.LENGTH_SHORT).show();
                openChooseVideoView();
            }
        });

    }

    private void uploadVideo() {
        //Video uploadedVideo = new Video();
        btnUploadVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TraineeUploadVideoActi.this, "da vao upload video", Toast.LENGTH_SHORT).show();
                checkVideoLength();
                confirmUploadingVideo();
            }
        });
    }

    private void checkVideoLength() {
        double frameRate = 0;
        try {
            SeekableByteChannel bc = (SeekableByteChannel) NIOUtils.readableFileChannel(String.valueOf(selectedPath));
            DemuxerTrack vt;
            try (MP4Demuxer dm = new MP4Demuxer((org.jcodec.common.io.SeekableByteChannel) bc)) {
                vt = dm.getVideoTrack();
            }
            frameRate = vt.getMeta().getTotalDuration();
            if (frameRate > 30) {
                Toast.makeText(this, "Please input again ", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Okkkkk ", Toast.LENGTH_SHORT).show();
            }
            System.out.println("frame rate ===== " + frameRate);
        } catch (Exception e) {

        }
    }

    private String createFolderName(String username) {
        return username + "-" + System.currentTimeMillis();
    }

    private String createdTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = Calendar.getInstance().getTime();
        return sdf.format(date);
    }

    private void uploadFileToFirebase() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        //final Video uploadedVideo = new Video();
        progressDialog.setTitle("Uploading");
        progressDialog.show();
        Log.e("TextView = ", txtVideoName.getText().toString());

        final String folderName = createFolderName("trainee01");

        StorageReference stR = storageReference.child(folderName + "/" + txtVideoName.getText().toString());
        stR.putFile(selectedVideoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Suggestion suggestion = new Suggestion();
                suggestion.setAccountId(3);
                suggestion.setVideoId(3);
                suggestion.setFoldernameTrainee(folderName);
                suggestion.setStatus("active");
                suggestion.setCreatedTime(createdTime());
                suggestion.setUrlVideoTrainee(taskSnapshot.getDownloadUrl().toString());

                SuggestionService suggestionService = new SuggestionService();
                suggestionService.createSuggestion(suggestion);
                //progressDialog.dismiss();
                Toast.makeText(TraineeUploadVideoActi.this, "File Uploaded", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(TraineeUploadVideoActi.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                progressDialog.setMessage("Uploaded " + (int) progress + "%...");
                if((int) progress == 100){
                    progressDialog.dismiss();
                }
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


    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

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
}
