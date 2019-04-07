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
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IVideoService {
    @POST("video/create")
    Call<Void> createVideo(@Header(Constants.header_string) String token, @Body Video video);

    @GET("video/getAllVideosByDate")
    Call<List<VideoDTO>> getVideosByDate(@Query("page") int page, @Query("size") int size);


    @GET("video/getAllVideosRelatedByCourseId")
    Call<List<VideoDTO>> getAllVideosRelatedByCourseId(@Query("courseId") int courseId,
                                                       @Query("currentVideoId") int currentVideoId);

    @GET("video/getAllVideosByTopNumOfView")
    Call<List<VideoDTO>> getAllVideosByTopNumOfView(@Query("page") int page, @Query("size") int size);

    @GET("video/getAllBoughtVideosByCourseId")
    Call<List<VideoDTO>> getAllBoughtVideosByCourseId(@Header(Constants.header_string) String token,
                                                      @Query("page") int page, @Query("size") int size,
                                                      @Query("traineeId") int traineeId,
                                                      @Query("courseId") int courseId);
    @GET("video/getAllUnBoughtVideoByCourseId")
    Call<List<VideoDTO>> getAllUnBoughtVideoByCourseId(@Header(Constants.header_string) String token,
                                                      @Query("page") int page, @Query("size") int size,
                                                       @Query("traineeId") int traineeId,
                                                       @Query("courseId") int courseId);

    @GET("video/getAllBoughtVideoRelated")
    Call<List<VideoDTO>> getAllBoughtVideoRelated(@Header(Constants.header_string) String token,
                                                  @Query("traineeId") int traineeId,
                                                  @Query("courseId") int courseId,
                                                  @Query("videoId") int videoId);

   /* @GET("video/videosByAccount")
    Call<List<VideoDTO>> getVideosByAccountId(@Header(Constants.header_string) String token, @)*/

    @GET("video/getAllFreeVideosByAccount")
    Call<List<Video>> getAllFreeVideosByAccount(@Query("accountId") int accountId);
    @GET("video/searchOrderByDate")
    Call<List<VideoDTO>> searchVideoOrderByDate(@Query("searchValue") String searchValue);
    @GET("video/searchOrderByView")
    Call<List<VideoDTO>> searchVideoOrderByView(@Query("searchValue") String searchValue);

    @GET("video/getAllVideoByCourseIdToEdit")
    Call<List<VideoDTO>> getAllVideoByCourseIdToEdit(@Header(Constants.header_string) String token,@Query("courseId") int courseId);

    @PUT("video/editVideo")
    Call<Boolean> editVideo(@Header(Constants.header_string) String token,@Body Video video);

    @PUT("video/changeNumberOfViewByVideoId/{videoId}")
    Call<Boolean> changeNumberOfViewByVideoId(@Header(Constants.header_string) String token,@Path("videoId") long videoId);
}
