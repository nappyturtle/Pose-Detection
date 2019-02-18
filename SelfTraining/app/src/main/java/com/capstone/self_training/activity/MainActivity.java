package com.capstone.self_training.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.capstone.self_training.R;

public class MainActivity extends AppCompatActivity {

    private Button btnChooseFile;
    private Button btnTrainerUpVideoActi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnChooseFile = (Button) findViewById(R.id.btnChooseFile);
        btnTrainerUpVideoActi = (Button) findViewById(R.id.btnTrainerUpVideoActi);

        final Intent intent = new Intent(this, TraineeUploadVideoActi.class);
        btnChooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });
        btnTrainerUpVideoActi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TrainerUploadVideoActi.class);
                startActivity(intent);
            }
        });

    }
}
