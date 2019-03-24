package com.capstone.self_training.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.capstone.self_training.R;
import com.capstone.self_training.activity.TraineeUploadVideoActi;
import com.capstone.self_training.activity.TrainerUploadVideoActi;
import com.capstone.self_training.camera.AutoFitTextureView;
import com.capstone.self_training.camera.CameraVideoFragment;
import com.coremedia.iso.IsoFile;
import com.coremedia.iso.boxes.Container;
import com.coremedia.iso.boxes.TimeToSampleBox;
import com.coremedia.iso.boxes.TrackBox;
import com.googlecode.mp4parser.DataSource;
import com.googlecode.mp4parser.FileDataSourceImpl;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Mp4TrackImpl;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CameraFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CameraFragment extends CameraVideoFragment {

    private static final String TAG = "CameraFragment";
    private static final String VIDEO_DIRECTORY_NAME = "Seft_Training";
    private SharedPreferences mPerferences;
    private SharedPreferences.Editor mEditor;
    // @BindView(R.id.mTextureView)
    AutoFitTextureView mTextureView;
    // @BindView(R.id.mRecordVideo)
    ImageView mRecordVideo;
    // @BindView(R.id.mVideoView)
    VideoView mVideoView;
    //@BindView(R.id.mPlayVideo)
    ImageView mPlayVideo;
    Unbinder unbinder;
    private String mOutputFilePath;
    //@BindView(R.id.editText_CameraFragment)
    EditText editText_Camera;
    View view;
    List<TrackBox> trackBoxes;
    CountDownTimer timer;
    CountDownTimer mTimer;
    Handler handler = new Handler(Looper.getMainLooper());
    SpeechRecognizer mSpeechRecognizer;
    Intent mSpeechRecognizerIntent;

    public CameraFragment() {
        // Required empty public constructor
    }


    public static CameraFragment newInstance() {
        CameraFragment fragment = new CameraFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    Runnable test = new Runnable() {
        @Override
        public void run() {
            //do work
            if (mPerferences == null) {

            } else {
                Activity activity = getActivity();
                if (isAdded() && activity != null) {
                    mSpeechRecognizer.stopListening();
                    mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                    mEditor.putString(getString(R.string.testSpeech), editText_Camera.getText().toString());
                    mEditor.apply();
                    editText_Camera.setText("");
                }

                handler.postDelayed(test, 6000); //wait 6 sec and run again
            }
        }
    };

    public void stopTest() {
        handler.removeCallbacks(test);
        super.onDestroyView();
    }

    public void startTest() {
        handler.postDelayed(test, 1500); //wait 1.5 ms and run
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermission();

        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this.getContext());
        mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault());


        timer = new CountDownTimer(1500, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (getContext() == null) {

                } else {
                    mPerferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                    mEditor = mPerferences.edit();
                }

                this.start();
            }
        };
        timer.start();

        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onResults(Bundle results) {
                //get all the matches
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                //displaying the first match
                if (matches != null)
                    editText_Camera.setText(matches.get(0));
            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_camera, container, false);
        mRecordVideo = (ImageView) view.findViewById(R.id.mRecordVideo);
        unbinder = ButterKnife.bind(this, view);
        editText_Camera = (EditText) view.findViewById(R.id.editText_CameraFragment);
        mPerferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        mEditor = mPerferences.edit();
        startTest();
        mTimer = new CountDownTimer(4000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                switch (editText_Camera.getText().toString()) {
                    case "quay":
                        if (mIsRecordingVideo) {
                            Toast.makeText(getContext(), "Đang quay..", Toast.LENGTH_SHORT).show();
                        } else {
                            startRecordingVideo();
                            mRecordVideo.setImageResource(R.drawable.ic_stop);
                            //Receive out put file here
                            mOutputFilePath = getCurrentFile().getAbsolutePath();
                        }
                        break;
                    case "bắt đầu":
                        if (mIsRecordingVideo) {
                            Toast.makeText(getContext(), "Đang quay..", Toast.LENGTH_SHORT).show();
                        } else {
                            startRecordingVideo();
                            mRecordVideo.setImageResource(R.drawable.ic_stop);
                            //Receive out put file here
                            mOutputFilePath = getCurrentFile().getAbsolutePath();
                        }
                        break;
                    case "dừng lại":
                        try {
                            stopRecordingVideo();
                            stopTest();
                            try {
                                mOutputFilePath = parseVideo(mOutputFilePath);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            mRecordVideo.setImageResource(R.drawable.ic_record);
                            timer.cancel();
                            mTimer.cancel();
                            mSpeechRecognizer.cancel();
                            try {
                                mSpeechRecognizer.destroy();
                            } catch (Exception e) {
                                Log.e(TAG, "Exception:" + e.toString());
                            }
                            Intent i = new Intent(getContext(), TraineeUploadVideoActi.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case "stop":
                        try {
                            stopRecordingVideo();
                            stopTest();
                            try {
                                mOutputFilePath = parseVideo(mOutputFilePath);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            mRecordVideo.setImageResource(R.drawable.ic_record);
                            timer.cancel();
                            mTimer.cancel();
                            mSpeechRecognizer.cancel();
                            try {
                                mSpeechRecognizer.destroy();
                            } catch (Exception e) {
                                Log.e(TAG, "Exception:" + e.toString());
                            }
                            Intent i = new Intent(getContext(), TraineeUploadVideoActi.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                }
                this.start();
            }
        }.start();
        return view;
    }

    @Override
    public int getTextureResource() {
        return R.id.mTextureView;
    }

    @Override
    protected void setUp(View view) {

    }


    @OnClick({R.id.mRecordVideo, R.id.mPlayVideo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mRecordVideo:
                /**
                 * If media is not recoding then start recording else stop recording
                 */
                if (mIsRecordingVideo) {
                    try {
                        stopRecordingVideo();
                        stopTest();
                        try {
                            mOutputFilePath = parseVideo(mOutputFilePath);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mRecordVideo.setImageResource(R.drawable.ic_record);
                        timer.cancel();
                        mTimer.cancel();
                        mSpeechRecognizer.cancel();
                        try {
                            mSpeechRecognizer.destroy();
                        } catch (Exception e) {
                            Log.e(TAG, "Exception:" + e.toString());
                        }
                        Intent i = new Intent(getContext(), TraineeUploadVideoActi.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    startRecordingVideo();
                    mRecordVideo.setImageResource(R.drawable.ic_stop);
                    //Receive out put file here
                    mOutputFilePath = getCurrentFile().getAbsolutePath();
                }
                break;
            case R.id.mPlayVideo:
                mVideoView.start();
                mPlayVideo.setVisibility(View.GONE);
                break;
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private String parseVideo(String mFilePath) throws IOException {
        DataSource channel = new FileDataSourceImpl(mFilePath);
        IsoFile isoFile = new IsoFile(channel);
        trackBoxes = isoFile.getMovieBox().getBoxes(TrackBox.class);
        boolean isError = false;
        for (TrackBox trackBox : trackBoxes) {
            TimeToSampleBox.Entry firstEntry = trackBox.getMediaBox().getMediaInformationBox().getSampleTableBox().getTimeToSampleBox().getEntries().get(0);
            // Detect if first sample is a problem and fix it in isoFile
            // This is a hack. The audio deltas are 1024 for my files, and video deltas about 3000
            // 10000 seems sufficient since for 30 fps the normal delta is about 3000
            if (firstEntry.getDelta() > 10000) {
                isError = true;
                firstEntry.setDelta(3000);
            }
        }
        File file = getOutputMediaFile();
        String filePath = file.getAbsolutePath();
        if (isError) {
            Movie movie = new Movie();
            for (TrackBox trackBox : trackBoxes) {
                movie.addTrack(new Mp4TrackImpl(channel.toString() + "[" + trackBox.getTrackHeaderBox().getTrackId() + "]", trackBox));
            }
            movie.setMatrix(isoFile.getMovieBox().getMovieHeaderBox().getMatrix());
            Container out = new DefaultMp4Builder().build(movie);

            //delete file first!
            FileChannel fc = new RandomAccessFile(filePath, "rw").getChannel();
            out.writeContainer(fc);
            fc.close();
            Log.d(TAG, "Finished correcting raw video");
            return filePath;
        }
        return mFilePath;
    }

    /**
     * Create directory and return file
     * returning video file
     */
    private File getOutputMediaFile() {
        // External sdcard file location
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(),
                VIDEO_DIRECTORY_NAME);
        // Create storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create "
                        + VIDEO_DIRECTORY_NAME + " directory");
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;

        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "VID_" + timeStamp + ".mp4");
        return mediaFile;
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!(ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + getContext()));
                getContext().startActivity(intent);
            }
        }
    }
}
