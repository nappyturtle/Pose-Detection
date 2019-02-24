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

public interface IVideoService {
    @POST("video/create")
    Call<Void> createVideo(@Header(Constants.header_string) String token, @Body Video video);

    @GET("video/getAllVideosByDate")
    Call<List<VideoDTO>> getVideosByDate();
}
