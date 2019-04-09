package com.capstone.self_training.activity;


import android.annotation.SuppressLint;
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
    }
    @Override
    public void onBackPressed() {

        finish();

    }
}