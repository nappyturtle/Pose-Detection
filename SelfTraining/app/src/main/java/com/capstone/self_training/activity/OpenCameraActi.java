package com.capstone.self_training.activity;


import android.annotation.SuppressLint;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;

import com.capstone.self_training.R;
import com.capstone.self_training.fragment.CameraFragment;

public class OpenCameraActi extends AppCompatActivity {
    Bundle bundle;

    @SuppressLint("ClickableViewAccessibility")
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_camera);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        CameraFragment cmr = CameraFragment.newInstance();
        getFragmentManager().beginTransaction()
                .replace(R.id.OpenCamera, cmr)
                .commit();
        AudioManager audioManager;
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        audioManager.startBluetoothSco();
        audioManager.setBluetoothScoOn(true);
    }

    public void OnBackPressed(){
        finish();
    }
//    public void onDestroy() {
//
//        if (bundle == null && getIntent().getExtras() == null) {
//        } else {
//            bundle = (Bundle) getIntent().getExtras().get("result");
//            Intent returnIntent = new Intent();
//            returnIntent.putExtra("resultVideo", bundle);
//            setResult(TraineeUploadVideoActi.RESULT_OK, returnIntent);
//            finish();
//        }
//        super.onDestroy();
//    }

}