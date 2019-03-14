package com.capstone.self_training.service.iService;

import com.capstone.self_training.dto.EnrollmentDTO;
import com.capstone.self_training.model.Enrollment;
import com.capstone.self_training.util.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface IEnrollmentService {
    @GET("enrollment/getAllBoughtCourse")
    Call<List<EnrollmentDTO>> getAllBoughtCourseByDate(@Header(Constants.header_string) String token, @Query("page") int page, @Query("size") int size, @Query("accountId") int accountId);

    @GET("enrollment/getAllBoughtCourseTrainer")
    Call<List<EnrollmentDTO>> getAllBoughtCourseTrainername(@Header(Constants.header_string) String token,@Query("accountId") int accountId);

    @POST("enrollment/createEnrollment")
    Call<Boolean> createEnrollment(@Header(Constants.header_string) String token, @Body Enrollment enrollment);
}
