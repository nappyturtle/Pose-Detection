package com.capstone.self_training.service.dataservice;

import com.capstone.self_training.model.Video;
import com.capstone.self_training.service.iService.IVideoService;

import retrofit2.Call;

public class VideoService implements IVideoService {

    private IVideoService iVideoService;

    @Override
    public Call<Void> createVideo(Video video) {
        iVideoService = DataService.getVideoService();
        Call<Void> call = iVideoService.createVideo(video);
        try{
            call.execute().body();
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
