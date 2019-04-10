package com.capstone.self_training.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.capstone.self_training.service.dataservice.CourseService;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Course extends Fragment {

    private RecyclerView home_course_list;
    private HomeCourseAdapter homeCourseAdapter;
    private List<CourseDTO> courseDTOList;
    private CourseService courseService;
    View view;
    SharedPreferences mPerferences;
    int accountId;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_course, container, false);

        init();
        return view;
    }

    private void init() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        mPerferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        accountId = mPerferences.getInt(getString(R.string.id),0);
        home_course_list = view.findViewById(R.id.home_course_list);
        courseService = new CourseService();
        if (courseDTOList == null) {
            courseDTOList = new ArrayList<>();
        }
//        courseDTOList = courseService.getAllCourse();
        //VuVG - 15/03/2019 - thay getAll = get những course chưa mua
        courseDTOList = courseService.getUnboughtCourses(accountId);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        home_course_list.setLayoutManager(layoutManager);
        homeCourseAdapter = new HomeCourseAdapter(courseDTOList, getContext(), accountId);
        home_course_list.setAdapter(homeCourseAdapter);
    }
    //refresh dữ liệu khi resume (sau khi login hay gì đó)

    @Override
    public void onResume() {
        super.onResume();
        init();
//        Toast.makeText(getContext(), "Account: " + accountId, Toast.LENGTH_SHORT).show();
    }
}
