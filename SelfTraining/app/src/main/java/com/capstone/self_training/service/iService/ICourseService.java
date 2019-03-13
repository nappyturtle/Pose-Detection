package com.capstone.self_training.service.iService;

import com.capstone.self_training.dto.CourseDTO;
import com.capstone.self_training.model.Course;
import com.capstone.self_training.util.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ICourseService {
    @GET("course/getAllCoursesByAccountId")
    Call<List<Course>> getAllCoursesByAccountId(@Header(Constants.header_string) String token, @Query("accountId") int accountId);

    @POST("course/create")
    Call<Void> createCourse(@Header(Constants.header_string) String token, @Body Course course);

    @GET("course/getAllCourses")
    Call<List<CourseDTO>> getAllCourse();

}
