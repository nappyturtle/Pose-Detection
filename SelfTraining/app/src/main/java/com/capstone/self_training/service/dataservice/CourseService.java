package com.capstone.self_training.service.dataservice;

import android.util.Log;

import com.capstone.self_training.dto.CourseDTO;
import com.capstone.self_training.model.Course;
import com.capstone.self_training.service.iService.ICourseService;

import java.util.ArrayList;
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

    public List<CourseDTO> getAllCourseByTrainerId(String token, int page, int size,int id) {
        List<CourseDTO> courses = null;
        Call<List<CourseDTO>> call = iCourseService.getAllCourseByTrainerId(token, page, size, id);
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

    public boolean editCourse(String token, Course course) {
        Call<Boolean> call = iCourseService.editCourse(token, course);
        boolean checked = false;
        try {
            checked = call.execute().body();
        } catch (Exception e) {
            Log.e("CourseService editCourse = ", e.getMessage());
        }
        return checked;
    }

    public List<CourseDTO> getAllCourse() {
        Call<List<CourseDTO>> call = iCourseService.getAllCourse();
        List<CourseDTO> list = new ArrayList<>();
        try {
            list = call.execute().body();
        } catch (Exception e) {
            Log.e("CourseService getAllCourse = ", e.getMessage());
        }
        return list;
    }

    public List<CourseDTO> getAllCoursesWithPriceByAccountId(int accountId) {
        Call<List<CourseDTO>> call = iCourseService.getAllCoursesWithPriceByAccountId(accountId);
        List<CourseDTO> list = new ArrayList<>();
        try {
            list = call.execute().body();
        } catch (Exception e) {
            Log.e("CourseService getAllCoursesWithPriceByAccountId = ", e.getMessage());
        }
        return list;
    }
    public List<CourseDTO> getUnboughtCourses(int accountId) {
        List<CourseDTO> courses = null;
        Call<List<CourseDTO>> call = iCourseService.getUnboughtCourses(accountId);
        try {
            courses = call.execute().body();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return courses;
    }
    public List<CourseDTO> searchCourseByName(String searchValue, int accountId) {
        List<CourseDTO> courses = null;
        Call<List<CourseDTO>> call = iCourseService.searchCourseByName(searchValue, accountId);
        try {
            courses = call.execute().body();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return courses;
    }

}
