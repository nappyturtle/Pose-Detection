package com.capstone.self_training.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.capstone.self_training.R;
import com.capstone.self_training.dto.CourseDTO;
import com.capstone.self_training.helper.TimeHelper;
import com.capstone.self_training.model.Enrollment;
import com.capstone.self_training.service.dataservice.EnrollmentService;
import com.squareup.picasso.Picasso;

public class ConfirmationActivity extends AppCompatActivity {
    private ImageView imgCourse;
    private TextView txtTitle;
    private TextView txtTrainername;
    private TextView txtPrice;
    private Button btnComeback;
    private CourseDTO dto;
    private SharedPreferences mSharedPreferences;
    private String token;
    private Toolbar toolbar;
    private int accountId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
        reflect();
        getDataFromIntent();
        backToHome();

    }

    private void backToHome() {
        btnComeback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity_Home.class);
                startActivity(intent);
            }
        });
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        dto = (CourseDTO) intent.getSerializableExtra("courseDTO");
        Picasso.get().load(dto.getCourse().getThumbnail()).fit().into(imgCourse);
        txtTitle.setText(dto.getCourse().getName());
        txtTrainername.setText(dto.getTrainerName());
        txtPrice.setText(dto.getCourse().getPrice() + ".000 VNĐ");

        Enrollment enrollment = new Enrollment(dto.getCourse().getId(), accountId, dto.getCourse().getPrice(), TimeHelper.getCurrentTime());
        EnrollmentService enrollmentService = new EnrollmentService();
        if (enrollmentService.createEnrollment(token, enrollment)) {
            Toast.makeText(this, "Bạn đã đăng kí thành công", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Hệ thống đang gặp sự cố!!!!!", Toast.LENGTH_SHORT).show();
        }
    }


    private void reflect() {
        imgCourse = (ImageView) findViewById(R.id.img_confirmPayment);
        txtTitle = (TextView) findViewById(R.id.txtNameCourse_confirmPayment);
        txtTrainername = (TextView) findViewById(R.id.txtTrainer_confirmPayment);
        txtPrice = (TextView) findViewById(R.id.price_confirmPayment);
        btnComeback = (Button) findViewById(R.id.btnBackToHome_confirmPayment);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        token = mSharedPreferences.getString(getString(R.string.token), "");
        accountId = mSharedPreferences.getInt(getString(R.string.id), 0);
        toolbar = (Toolbar) findViewById(R.id.toolbar_confirmPayment);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "XIn vui lòng hãy nhấn nút quay lại trang chủ", Toast.LENGTH_SHORT).show();

    }
}
