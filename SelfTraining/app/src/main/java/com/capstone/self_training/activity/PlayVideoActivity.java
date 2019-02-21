package com.capstone.self_training.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.capstone.self_training.util.TransformDataUtil;
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

    TextView video_title, video_view, username;
    CircleImageView user_img;
    Button user_sub_btn;
    private Button btnUpSelfTrainVideo;


    private RecyclerView relate_video_list;
    private RecyclerView.Adapter relateVideoAdapter;
    private List<Video> videos;

    /***** HARD DATA *****/

    /*private static final String[] thumbnail_list_demo = {
            "https://kenh14cdn.com/zoom/700_438/2018/11/27/5b4083abbcf59-blackpink-instagram-photo-music-core-win-white-outfit-2-1543314360824254878270-crop-1549777736947858310470.jpg",
            "https://toquoc.mediacdn.vn/2019/1/7/yq-blackpink-040120192x2x-1546846556026543083655.jpg",
            "https://upload.wikimedia.org/wikipedia/commons/0/00/%EB%B8%94%EB%9E%99%ED%95%91%ED%81%AC%28BlackPink%29_-_%EB%A7%88%EC%A7%80%EB%A7%89%EC%B2%98%EB%9F%BC_171001_%EC%BD%94%EB%A6%AC%EC%95%84%EB%AE%A4%EC%A7%81%ED%8E%98%EC%8A%A4%ED%8B%B0%EB%B2%8C.jpg",
            "https://photo-2-baomoi.zadn.vn/w1000_r1/2018_09_22_329_27832729/035959bf66fe8fa0d6ef.jpg",
            "https://img.jakpost.net/c/2016/08/09/2016_08_09_9562_1470736144._large.jpg",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQcpfXOvNQE3kBrDVi0i3VDlXTB_SeLN_iTMWn2RXk5v20nuYML",
            "https://allaboutkpop.net/wp-content/uploads/2019/01/4f5f4e4d41744da1929583307b6d3e78.jpeg",
            "https://1.bp.blogspot.com/-rqwR3IAKmuw/WyXVE1_8lJI/AAAAAAAAxcA/QUcSuDzoHbUerNlLFl_eF-HQ5zCLQRobQCLcBGAs/s1600/201806170922773667_5b25afa248729.jpg",
            "https://akns-images.eonline.com/eol_images/Entire_Site/20181028/rs_600x600-181128235449-e-asia-blackpink-fave-fashion-trends-thumbnail.jpg?fit=around|700:700&crop=700:700;center,top&output-quality=90",
            "http://www.kpoppro.com/wp-content/uploads/2018/06/BLACKPINK10-1.jpg"
    };

    private static final String[] video_list_demo = {
            "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4",
            "http://techslides.com/demos/sample-videos/small.mp4",
            "https://thepaciellogroup.github.io/AT-browser-tests/video/ElephantsDream.mp4",
            "http://www.html5videoplayer.net/videos/toystory.mp4",
            "http://mirrors.standaloneinstaller.com/video-sample/jellyfish-25-mbps-hd-hevc.mp4",
            "http://mirrors.standaloneinstaller.com/video-sample/lion-sample.mp4",
            "http://mirrors.standaloneinstaller.com/video-sample/Panasonic_HDC_TM_700_P_50i.mp4",
            "http://mirrors.standaloneinstaller.com/video-sample/dolbycanyon.mp4",
            "http://mirrors.standaloneinstaller.com/video-sample/star_trails.mp4",
            "http://mirrors.standaloneinstaller.com/video-sample/TRA3106.mp4"
    };

    private static final String[] account_demo = {
            "Ngoc",
            "Hau",
            "Minh",
            "Sang",
            "Hoang",
            "Kiet",
            "Vu",
            "Phat",
            "Thinh",
            "Hieu"
    };*/

    /*private static final Account account = new Account(
            1,
            "hoangtlt",
            "acbxyz123",
            "hoang@gmail.com",
            "0123456789",
            "Nam",
            "https://scontent.fsgn5-4.fna.fbcdn.net/v/t1.0-9/10600429_359175494240981_2588390037092242108_n.jpg?_nc_cat=102&_nc_oc=AQlg2VkCpatLNfOJdMqfBxEvoEq200uTJF0adkKZpuIsE_ew6RGgRdcfKNiloh1Kf0c&_nc_ht=scontent.fsgn5-4.fna&oh=ea12de05ba33e04e6ce4ed95091b663f&oe=5CE453BF",
            "1358/28/14 Quang Trung",
            null,
            null,
            null,
            null,
            null
    );*/

    /***************************/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isPauseMedia = false;
        super.onCreate(savedInstanceState);

        final Video playingVideo = (Video) getIntent().getSerializableExtra("PLAYVIDEO");
        Account account = (Account) getIntent().getSerializableExtra("ACCOUNT");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        VideoService videoService = new VideoService();
        List<VideoDTO> list = videoService.getVideosByDate();

        if (isFullScreen() == false) {
            setContentView(R.layout.activity_play_video);

            video_title = (TextView) findViewById(R.id.play_video_title);
            video_view = (TextView) findViewById(R.id.play_video_view);
            username = (TextView) findViewById(R.id.play_video_username);
            video_title.setText(playingVideo.getTitle());
            video_view.setText(playingVideo.getNumOfView() + " lượt xem");
            if (account.getUsername() != null) {
                username.setText(account.getUsername());
            }

            /*user_sub_btn = (Button) findViewById(R.id.play_video_sub);
            user_sub_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(PlayVideoActivity.this, "Subscribe clicked", Toast.LENGTH_SHORT).show();
                }
            });*/

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

            /** RERLATE VIDEO BINDING **/
            relate_video_list = (RecyclerView) findViewById(R.id.relate_video_list);
            relate_video_list.setHasFixedSize(true);
            relate_video_list.setLayoutManager(new LinearLayoutManager(this));
            videos = new ArrayList<>();

            /** SET HARD CODE **/

            /*for (int i = 0; i < 10; i++) {
                Video video = new Video(
                        1,
                        "Demo display long name video about yoga thumbnail in list " + i,
                        thumbnail_list_demo[i],
                        video_list_demo[i],
                        1,
                        20,
                        107812,
                        "enable",
                        account_demo[i],
                        "",
                        "",
                        "",
                        "",
                        null,
                        null,
                        null
                );
                videos.add(video);

            }*/
            /****************************/

            relateVideoAdapter = new RelateVideoAdapter(list, this);

            relate_video_list.setAdapter(relateVideoAdapter);

            /** END BINDING RELATE VIDEO **/


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


    //Show the media controller
    @Override
    public boolean onTouchEvent(MotionEvent event) {
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
    }
    // End SurfaceHolder.Callback

    // Implement MediaPlayer.OnPreparedListener
    @Override
    public void onPrepared(MediaPlayer mp) {
        controller.setMediaPlayer(this);
        controller.setAnchorView((FrameLayout) findViewById(R.id.videoSurfaceContainer));
        mediaPlayer.start();
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