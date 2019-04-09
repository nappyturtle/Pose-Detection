package com.capstone.self_training.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.capstone.self_training.R;
import com.capstone.self_training.adapter.RelateVideoAdapter;
import com.capstone.self_training.dto.VideoDTO;
import com.capstone.self_training.model.Account;
import com.capstone.self_training.model.Video;
import com.capstone.self_training.service.dataservice.VideoService;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PlayVideoActivity extends AppCompatActivity implements SurfaceHolder.Callback,
        MediaPlayer.OnPreparedListener, VideoControllerView.MediaPlayerControl {
    private String TAG = "FullscreenVideoActivity";
    SurfaceView videoSurface;
    private MediaPlayer mediaPlayer;
    private VideoControllerView controller;
    private boolean isPauseMedia;
    private Toolbar toolbar;

    TextView video_title, video_view, username;
    CircleImageView user_img;
    Button user_sub_btn;
    private Button btnUpSelfTrainVideo;

    private SharedPreferences mSharedPreferences;
    private RecyclerView relate_video_list;
    private RecyclerView.Adapter relateVideoAdapter;
    private List<Video> videos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isPauseMedia = false;
        super.onCreate(savedInstanceState);

        final Video playingVideo = (Video) getIntent().getSerializableExtra("PLAYVIDEO");
        Account account = (Account) getIntent().getSerializableExtra("ACCOUNT");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String token = mSharedPreferences.getString(getString(R.string.token), "");
        VideoService videoService = new VideoService();
        List<VideoDTO> list = videoService.getAllVideosRelatedByCourseId(playingVideo.getCourseId(), playingVideo.getId());

        if (videoService.changeNumberOfViewByVideoId(token, playingVideo.getId())) {
            Log.e("Message = ", "true");
        } else {
            Log.e("Message = ", "false");
        }
        if (isFullScreen() == false) {
            setContentView(R.layout.activity_play_video);

            video_title = (TextView) findViewById(R.id.play_video_title);
            video_view = (TextView) findViewById(R.id.play_video_view);
            username = (TextView) findViewById(R.id.play_video_username);
            toolbar = (Toolbar) findViewById(R.id.playVideo_toolbar_id);
            video_title.setText(playingVideo.getTitle());
            video_view.setText(playingVideo.getNumOfView() + " lượt xem");
            if (account.getUsername() != null) {
                username.setText(account.getUsername());
            }


            btnUpSelfTrainVideo = (Button) findViewById(R.id.btnUpSelfTrainVideo);
            btnUpSelfTrainVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), TraineeUploadVideoActi.class);
                    intent.putExtra("PLAYINGVIDEO", playingVideo);
                    startActivity(intent);
                }
            });

            user_img = (CircleImageView) findViewById(R.id.play_video_userImg);

            Picasso.get().load(account.getImgUrl()).fit().into(user_img);
            user_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(PlayVideoActivity.this, "View Profile Clicked", Toast.LENGTH_SHORT).show();
                }
            });

            relate_video_list = (RecyclerView) findViewById(R.id.relate_video_list);
            relate_video_list.setHasFixedSize(true);
            relate_video_list.setLayoutManager(new LinearLayoutManager(this));
            videos = new ArrayList<>();


            relateVideoAdapter = new RelateVideoAdapter(list, this);

            relate_video_list.setAdapter(relateVideoAdapter);
            setupToolbar(playingVideo.getTitle());

        } else {
            setContentView(R.layout.activity_fullscreen_video);
        }


        videoSurface = (SurfaceView) findViewById(R.id.videoSurface);
        SurfaceHolder videoHolder = videoSurface.getHolder();

        videoHolder.addCallback(this);
        String videoPath = playingVideo.getContentUrl();
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

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.play_video_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_back:
                Intent intentChangePassword = new Intent(PlayVideoActivity.this, MainActivity_Home.class);
                startActivity(intentChangePassword);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupToolbar(String videoname) {
        setSupportActionBar(toolbar);
        toolbar.setTitle(videoname);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
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
        controller.setAnchorView((FrameLayout) findViewById(R.id.videoSurfaceContainer));
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
        try {
            mediaPlayer.prepareAsync();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            finish();
            startActivity(getIntent());
        }
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
        Log.d(TAG, "isFullScreen: " + isFullScreen);
        return isFullScreen;
    }

    @Override
    public void toggleFullScreen() {
        Log.d(TAG, "toggleFullScreen");
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
//        mediaPlayer.reset();
//        mediaPlayer.release();
//        super.onDestroy();
//    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isPauseMedia) {
            mediaPlayer.start();
        }
    }

}