package com.capstone.self_training.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.capstone.self_training.R;
import com.capstone.self_training.model.Video;
import com.capstone.self_training.util.Constants;
import com.capstone.self_training.util.MP4Demuxer;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jcodec.common.DemuxerTrack;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.io.SeekableByteChannel;

public class UploadVideoActi extends AppCompatActivity {

    private Button buttonChoose;
    private Button buttonUpload;
    private TextView textView;
    private TextView textViewResponse;
    private static final int SELECT_VIDEO = 3;
    private String selectedPath;
    private VideoView videoView;
    private String filename;
    private Uri selectedVideoUri;


    private StorageReference storageReference;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_video);
        Log.e("AAAAAAAA ", "da vao day ");
        reflect();
        requestRead();
        uploadVideo();
    }
    private void reflect(){
        buttonChoose = (Button) findViewById(R.id.buttonChoose);
        buttonUpload = (Button) findViewById(R.id.buttonUpload);
        textView = (TextView) findViewById(R.id.textView);
        textViewResponse = (TextView) findViewById(R.id.textViewResponse);
        videoView = (VideoView)findViewById(R.id.videoViewId);

        storageReference = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS);
    }
    private void chooseFileUpload(){

        buttonChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UploadVideoActi.this,"aaaaaaaaaaaaaaa",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select a Video "), SELECT_VIDEO);
            }
        });

    }
    private void uploadVideo(){
        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UploadVideoActi.this, "da vao upload video", Toast.LENGTH_SHORT).show();
                checkVideoLength();
                uploadFileToFirebase();
            }
        });
    }
    private void checkVideoLength(){
        double frameRate = 0;
        try {

            SeekableByteChannel bc = NIOUtils.readableFileChannel(String.valueOf(selectedPath));
            MP4Demuxer dm = new MP4Demuxer(bc);
            DemuxerTrack vt = dm.getVideoTrack();
            frameRate = vt.getMeta().getTotalDuration();
            Log.e("FFFFFFFFF = ",String.valueOf(frameRate));
            if(frameRate > 30){
                Toast.makeText(this,"Please input again ",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,"Okkkkk ",Toast.LENGTH_SHORT).show();
            }
            System.out.println("frame rate ===== "+frameRate);
        }catch (Exception e){

        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == SELECT_VIDEO && resultCode == RESULT_OK && data != null && data.getData() != null){
            System.out.println("Select video  ");
            selectedVideoUri = data.getData();
            selectedPath = getPath(selectedVideoUri);
            filename = selectedPath.substring(selectedPath.lastIndexOf("/")+1);
            textView.setText(filename);
            Uri uri = Uri.parse(selectedPath);
            videoView.setVideoURI(uri);
            MediaController mediaController = new MediaController(this);
            videoView.setMediaController(mediaController);
            mediaController.setAnchorView(videoView);
        }

    }

    public String getPath(Uri uri){
        Cursor cursor = getContentResolver().query(uri,null,null,null,null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":")+1);
        cursor.close();
        cursor = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,null,
                MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
        cursor.close();
        return path;
    }

    private void uploadFileToFirebase(){
        final String[] test = {""};
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading");
        progressDialog.show();
        Log.e("TextView = ",textView.getText().toString());
        StorageReference stR = storageReference.child(Constants.STORAGE_PATH_UPLOADS+textView.getText().toString());
        stR.putFile(selectedVideoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //progressDialog.dismiss();
                Toast.makeText(UploadVideoActi.this, "File Uploaded", Toast.LENGTH_SHORT).show();
                //Toast.makeText(UploadVideoActi.this, taskSnapshot.getDownloadUrl().toString(), Toast.LENGTH_SHORT).show();
                test[0] = taskSnapshot.getDownloadUrl().toString();
//                Video video = new Video(textView.getText().toString(),taskSnapshot.getDownloadUrl().toString());
//                String uploadId = mDatabase.push().getKey();
//                mDatabase.child(uploadId).setValue(video);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(UploadVideoActi.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                progressDialog.setMessage("Uploaded "+(int)progress + "%...");
            }
        });
        Toast.makeText(this, test[0]+"+++++++++++", Toast.LENGTH_SHORT).show();

    }


    // mở quyền camera
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

    /**
     * requestPermissions and do something
     *
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
                Toast.makeText(UploadVideoActi.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
