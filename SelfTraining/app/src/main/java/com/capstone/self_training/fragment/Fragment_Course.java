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

public class Fragment_Course extends Fragment {

    private RecyclerView home_course_list;
    private HomeCourseAdapter homeCourseAdapter;
    private List<CourseDTO> courseDTOList;
    private CourseService courseService;
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_course, container, false);
        init();
        return view;
    }

    private void init() {
        home_course_list = view.findViewById(R.id.home_course_list);
        courseService = new CourseService();
        if (courseDTOList == null) {
            courseDTOList = new ArrayList<>();
        }
        courseDTOList = courseService.getAllCourse();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        home_course_list.setLayoutManager(layoutManager);
        homeCourseAdapter = new HomeCourseAdapter(courseDTOList, getContext());
        home_course_list.setAdapter(homeCourseAdapter);
    }
}
