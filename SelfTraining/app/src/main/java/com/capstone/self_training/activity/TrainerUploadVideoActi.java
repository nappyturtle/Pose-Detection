package com.capstone.self_training.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

import com.capstone.self_training.R;
import com.capstone.self_training.model.Video;
import com.capstone.self_training.service.dataservice.VideoService;
import com.capstone.self_training.util.Constants;
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

import java.nio.channels.SeekableByteChannel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TrainerUploadVideoActi extends AppCompatActivity {

    private static int PICK_IMAGE_REQUEST = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private static final int SELECT_VIDEO = 3;

    private VideoView vwTrainerVideo;
    private Button btnTrainerChooseVideo;
    private Button btnTrainerUpVideo;
    private EditText edtTitle;
    private Spinner spnCategory;

    private StorageReference storageReference;

    private Uri selectedVideoUri;
    private String selectedPath;
    private String filename;

    private ImageView ivThumbnail;
    private Uri imageUri;
    Bitmap bmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_upload_video);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        init();
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
        spnCategory = (Spinner) findViewById(R.id.spnCategory);

        storageReference = FirebaseStorage.getInstance().getReference();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();
            Picasso.get().load(imageUri).into(ivThumbnail);
        }

        if (requestCode == SELECT_VIDEO && resultCode == RESULT_OK && data != null && data.getData() != null) {
            System.out.println("Select video  ");
            selectedVideoUri = data.getData();
            selectedPath = getPath(selectedVideoUri);
            filename = selectedPath.substring(selectedPath.lastIndexOf("/") + 1);
            edtTitle.setText(filename);
            Uri uri = Uri.parse(selectedPath);
            vwTrainerVideo.setVideoURI(uri);
            MediaController mediaController = new MediaController(this);
            vwTrainerVideo.setMediaController(mediaController);
            mediaController.setAnchorView(vwTrainerVideo);
        }
    }

    private void uploadVideo() {
        //Video uploadedVideo = new Video();
        btnTrainerUpVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TrainerUploadVideoActi.this, "da vao upload video", Toast.LENGTH_SHORT).show();
                checkVideoLength();
                uploadFileToFirebase();
                //videoService.createVideo(uploadedVideo);
            }
        });
    }

    private void chooseFileUpload() {

        btnTrainerChooseVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TrainerUploadVideoActi.this, "aaaaaaaaaaaaaaa", Toast.LENGTH_SHORT).show();
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
        Log.e("TextView = ", edtTitle.getText().toString());

        final String foldername = createFolderName("trainer01");

        final StorageReference stR = storageReference.child(foldername + "/" + edtTitle.getText().toString());

        stR.putFile(selectedVideoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //progressDialog.dismiss();
                Toast.makeText(TrainerUploadVideoActi.this, "File Uploaded", Toast.LENGTH_SHORT).show();
                //Toast.makeText(TraineeUploadVideoActi.this, taskSnapshot.getDownloadUrl().toString(), Toast.LENGTH_SHORT).show();
                final Video videoUploadedToFirebase = new Video(edtTitle.getText().toString(), taskSnapshot.getDownloadUrl().toString());

                //String uploadId = mDatabase.push().getKey();
                //mDatabase.child(uploadId).setValue(video);

                

                StorageReference srImg = storageReference.child(foldername + "/" + edtTitle.getText().toString() + "-thumbnail");
                srImg.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        videoUploadedToFirebase.setThumnailUrl(taskSnapshot.getDownloadUrl().toString());
                        VideoService videoService = new VideoService();
                        videoService.createVideo(videoUploadedToFirebase);
                        Toast.makeText(TrainerUploadVideoActi.this, "upload image success", Toast.LENGTH_LONG).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (50.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        progressDialog.setMessage("Uploaded " + (int) progress + "%...");
                        if ((int) progress == 50) {
                            progressDialog.dismiss();
                        }
                    }
                });

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
                progressDialog.setMessage("Uploaded " + (int) progress + "%...");
                if ((int) progress == 100) {
                    progressDialog.dismiss();
                }
            }
        });


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

}
