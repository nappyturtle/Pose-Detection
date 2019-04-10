package com.capstone.self_training.activity;

import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;

import com.capstone.self_training.R;
import com.capstone.self_training.dto.SuggestionDTO;
import com.capstone.self_training.model.Suggestion;
import com.capstone.self_training.util.CheckConnection;

import java.io.IOException;

public class TraineeVideoUploadedActivity extends AppCompatActivity implements SurfaceHolder.Callback,
        MediaPlayer.OnPreparedListener, VideoControllerView.MediaPlayerControl {
    private String TAG = "FullscreenTraineePlayingVideoActivity";
    SurfaceView surfaceView;
    private Toolbar toolbar;
    private MediaPlayer mediaPlayer;
    private VideoControllerView controller;
    private boolean isPauseMedia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isPauseMedia = false;
        super.onCreate(savedInstanceState);

        if (CheckConnection.haveNetworkConnection(this)) {
            //reflect();
            //surfaceView = (SurfaceView) findViewById(R.id.surface_video_trainee_uploaded_id);
            //toolbar = (Toolbar) findViewById(R.id.toolbar_trainee_playingVideo_id);
            //displayToolBar();
            if (isFullScreen() == false) {
                setContentView(R.layout.activity_trainee_video_uploaded);
                displayToolBar();

            } else {
                setContentView(R.layout.activity_fullscreen_trainee_video_uploaded);
            }

            SuggestionDTO suggestion = (SuggestionDTO) getIntent().getSerializableExtra("Suggestion");
            surfaceView = (SurfaceView) findViewById(R.id.surface_video_trainee_uploaded_id);
            SurfaceHolder videoHolder = surfaceView.getHolder();

            videoHolder.addCallback(this);
            String videoPath = suggestion.getUrlVideoTrainee();
            mediaPlayer = new MediaPlayer();
            controller = new VideoControllerView(this);

            try {
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setDataSource(this, Uri.parse(videoPath));
                mediaPlayer.setOnPreparedListener(this);
            } catch (IllegalArgumentException | SecurityException | IllegalStateException |
                    IOException e) {
                e.printStackTrace();
            }
        } else {
            CheckConnection.showConnection(this, "Kiểm tra kết nối internet");
        }
    }

    private void displayToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_trainee_playingVideo_id);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //Show the media controller
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        controller.setMediaPlayer(this);
        controller.setAnchorView((FrameLayout) findViewById(R.id.surfaceContainer_playingVideo_trainee_id));
        mediaPlayer.start();
        controller.show();
        return false;
    }

    // Implement SurfaceHolder.Callback
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mediaPlayer.setDisplay(holder);
        mediaPlayer.prepareAsync();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mediaPlayer.stop();
    }
    // End SurfaceHolder.Callback

    // Implement MediaPlayer.OnPreparedListener
    @Override
    public void onPrepared(MediaPlayer mp) {

    }
    // End MediaPlayer.OnPreparedListener

    // Implement VideoMediaController.MediaPlayerControl
    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    @Override
    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    @Override
    public void pause() {
        mediaPlayer.pause();
    }

    @Override
    public void seekTo(int i) {
        mediaPlayer.seekTo(i);
    }

    @Override
    public void start() {
        mediaPlayer.start();
    }

    @Override
    public boolean isFullScreen() {
        boolean isFullScreen = (getResources().getConfiguration().orientation == 2);
        Log.e(TAG, "isFullScreen: " + isFullScreen);
        return isFullScreen;
    }

    @Override
    public void toggleFullScreen() {
        Log.e(TAG, "toggleFullScreen");
        mediaPlayer.pause();
        if (getResources().getConfiguration().orientation == 1) {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.pause();
        isPauseMedia = true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    //    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        mediaPlayer.release();
//    }
    @Override
    protected void onResume() {
        super.onResume();
        if (isPauseMedia) {
            mediaPlayer.start();
        }
    }
}
