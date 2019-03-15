package com.capstone.self_training.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.capstone.self_training.R;
import com.capstone.self_training.dto.TrainerInfo;
import com.capstone.self_training.model.Account;
import com.capstone.self_training.service.dataservice.AccountService;
import com.capstone.self_training.service.dataservice.CourseService;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Fragment_Trainer_Channel_Intro extends Fragment {
    private CircleImageView cvTrainerImg;
    private TextView tvTrainerName;
    private TextView tvRegisterNumber, tvPhoneNumber, tvEmail, tvCourseNumber;
    private int trainerId;
    private TrainerInfo trainerInfo;

    private AccountService accountService;

    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_fragment__trainer__channel__intro, container, false);
        init();
        return view;
    }

    private void init() {
        cvTrainerImg = view.findViewById(R.id.intro_trainer_channel_userImg);
        tvTrainerName = view.findViewById(R.id.intro_trianer_channel_username);
        tvRegisterNumber = view.findViewById(R.id.intro_trainer_channel_regis_number);
        tvPhoneNumber = view.findViewById(R.id.intro_trainer_phone);
        tvEmail = view.findViewById(R.id.intro_trainer_email);
        tvCourseNumber = view.findViewById(R.id.intro_trainer_course_number);

        accountService = new AccountService(getContext());
        trainerInfo = new TrainerInfo();
        trainerInfo = accountService.getTrainerInfo(trainerId);

        if (trainerInfo != null) {
            Account account = new Account();
            account = trainerInfo.getAccount();

            if (!(account.getImgUrl() == null || account.getImgUrl().equalsIgnoreCase(""))) {
                Picasso.get().load(account.getImgUrl()).fit().into(cvTrainerImg);
            }

            tvTrainerName.setText(account.getUsername());
            tvRegisterNumber.setText(trainerInfo.getTotalRegister() + " người đăng kí");

            if (!(account.getPhone() == null || account.getPhone().equalsIgnoreCase(""))) {
                tvPhoneNumber.setText(account.getPhone());
            } else {
                tvPhoneNumber.setText("");
            }

            if (!(account.getEmail() == null || account.getEmail().equalsIgnoreCase(""))) {
                tvEmail.setText(account.getEmail());
            } else {
                tvEmail.setText("");
            }

            if (trainerInfo.getTotalCourse() == 0) {
                tvCourseNumber.setText("Chưa có khóa học nào");
            } else {
                tvCourseNumber.setText("Có " + trainerInfo.getTotalCourse() + " khóa học.");
            }
        }
    }

    public void getTrainerId(int accountId) {
        trainerId = accountId;
    }
}
