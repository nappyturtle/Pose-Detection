package com.capstone.self_training.service.dataservice;

import android.service.autofill.Dataset;
import android.util.Log;

import com.capstone.self_training.model.Course;
import com.capstone.self_training.service.iService.ICourseService;

import java.util.List;

import retrofit2.Call;

public class CourseService {
    private ICourseService iCourseService;

    public CourseService() {
        this.iCourseService = DataService.getCourseService();
    }

    public List<Course> getAllCoursesByAccountId(String token, int id) {
        List<Course> courses = null;
        Call<List<Course>> call = iCourseService.getAllCoursesByAccountId(token, id);
        try {
            courses = call.execute().body();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return courses;
    }

    public Call<Void> createCourse(String token, Course course) {
        Call<Void> call = iCourseService.createCourse(token, course);
        try {
            call.execute().body();
        } catch (Exception e) {
            Log.e("CourseService createCourse = ", e.getMessage());
        }
        return null;
    }
}
