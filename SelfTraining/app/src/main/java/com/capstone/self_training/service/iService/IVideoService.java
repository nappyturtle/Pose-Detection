package com.capstone.self_training.service.iService;

import com.capstone.self_training.dto.VideoDTO;
import com.capstone.self_training.model.Video;
import com.capstone.self_training.util.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface IVideoService {
    @POST("video/create")
    Call<Void> createVideo(@Header(Constants.header_string) String token, @Body Video video);

    @GET("video/getAllVideosByDate")
    Call<List<VideoDTO>> getVideosByDate(@Query("page") int page,@Query("size") int size);


    @GET("video/getAllVideosByTrainer")
    Call<List<VideoDTO>> getVideosByTrainer(@Query("accountId") int accountId);

    @GET("video/getAllVideosByTopNumOfView")
    Call<List<VideoDTO>> getAllVideosByTopNumOfView(@Query("page") int page,@Query("size") int size);

    @GET("video/getAllVideoByCourseId")
    Call<List<VideoDTO>> getAllBoughtVideosByCourseId(@Header(Constants.header_string) String token,
                                                      @Query("page") int page,@Query("size") int size,@Query("courseId") int courseId);
    @GET("video/getAllBoughtVideoRelated")
    Call<List<VideoDTO>> getAllBoughtVideoRelated(@Header(Constants.header_string) String token,
                                                  @Query("courseId") int courseId,@Query("videoId") int videoId);
}
