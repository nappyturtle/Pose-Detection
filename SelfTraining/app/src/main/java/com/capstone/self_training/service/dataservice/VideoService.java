package com.capstone.self_training.service.dataservice;

import com.capstone.self_training.model.Video;
import com.capstone.self_training.service.ApiUtils;
import com.capstone.self_training.service.apiservice.ApiService;
import com.capstone.self_training.service.iService.IVideoService;

import retrofit2.Call;
import retrofit2.Response;

public class VideoService implements IVideoService {

    private IVideoService iVideoService;

    @Override
    public Call<Void> createVideo(Video video) {
        iVideoService = ApiUtils.getVideoService();
        Call<Void> call = iVideoService.createVideo(video);
        try{
            call.execute().body();
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
