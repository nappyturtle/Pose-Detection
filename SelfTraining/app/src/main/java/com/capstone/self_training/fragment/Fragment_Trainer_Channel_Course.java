package com.capstone.self_training.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.capstone.self_training.R;
import com.capstone.self_training.adapter.HomeCourseAdapter;
import com.capstone.self_training.dto.CourseDTO;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_fragment__trainer__channel__course, container, false);
        init();
        return view;
    }

    private void init() {
        rc_trainer_channel_course = view.findViewById(R.id.rc_trainer_channel_course);
        courseService = new CourseService();
        if (courseDTOList == null) {
            courseDTOList = new ArrayList<>();
        }
        courseDTOList = courseService.getAllCoursesWithPriceByAccountId(trainerId);
        if (courseDTOList != null) {
            for (CourseDTO c : courseDTOList) {
                c.setTrainerName("");
            }
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            rc_trainer_channel_course.setLayoutManager(layoutManager);
            courseAdapter = new HomeCourseAdapter(courseDTOList, getContext(), trainerId);
            rc_trainer_channel_course.setAdapter(courseAdapter);
        }

    }

    public void getTrainerId(int accountId) {
        trainerId = accountId;
    }
}
