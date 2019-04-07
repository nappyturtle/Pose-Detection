package com.capstone.self_training.fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.capstone.self_training.R;
import com.capstone.self_training.adapter.HomeCourseAdapter;
import com.capstone.self_training.dto.CourseDTO;
import com.capstone.self_training.model.Account;
import com.capstone.self_training.service.dataservice.CourseService;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Trainer_Channel_Course extends Fragment {
    private RecyclerView rc_trainer_channel_course;
    private View view;
    private HomeCourseAdapter courseAdapter;
    List<CourseDTO> courseDTOList;
    private CourseService courseService;
    int trainerId;
    int currentUserId;
    String token;
    //Account accountExsited;
    private SharedPreferences mPerferences;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_fragment__trainer__channel__course, container, false);
        savedInstanceState = getArguments();
        trainerId = savedInstanceState.getInt("accountId");
        currentUserId = savedInstanceState.getInt("currentUserId");
        token = savedInstanceState.getString("token");
        mPerferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        init();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
        Toast.makeText(getContext(), "VUVG = "+currentUserId, Toast.LENGTH_SHORT).show();
        currentUserId = mPerferences.getInt(getString(R.string.id),0);
    }

    private void init() {
        rc_trainer_channel_course = view.findViewById(R.id.rc_trainer_channel_course);
        courseService = new CourseService();
//        if (courseDTOList == null) {
            courseDTOList = new ArrayList<>();
        //}
        List<CourseDTO> list = courseService.getAllCoursesWithPriceByAccountId(trainerId);
        for (CourseDTO c : list) {
            courseDTOList.add(c);
        }
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rc_trainer_channel_course.setLayoutManager(layoutManager);
        courseAdapter = new HomeCourseAdapter(courseDTOList, getContext(), trainerId, currentUserId,token);
        rc_trainer_channel_course.setAdapter(courseAdapter);

    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if(resultCode == -1 && data != null && data.getData() != null){
//            accountExsited = (Account) data.getSerializableExtra("Account");
//            currentUserId = accountExsited.getId();
//            Log.e("da quay lai fragment-course-trainer-channel", String.valueOf(currentUserId));
//        }
//    }


    public void getTrainerId(int accountId, int currentUserId) {
        trainerId = accountId;
        currentUserId = currentUserId;
    }

    public static Fragment_Trainer_Channel_Course newInstance(int accountId, int currentUserId, String token) {
        Fragment_Trainer_Channel_Course f = new Fragment_Trainer_Channel_Course();
        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putInt("accountId", accountId);
        args.putInt("currentUserId", currentUserId);
        args.putString("token", token);
        f.setArguments(args);
        return f;
    }
}
