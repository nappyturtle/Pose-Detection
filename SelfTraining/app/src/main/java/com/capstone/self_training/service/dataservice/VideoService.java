package com.capstone.self_training.service.dataservice;

import com.capstone.self_training.dto.VideoDTO;
import com.capstone.self_training.model.Video;
import com.capstone.self_training.service.iService.IVideoService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class VideoService {

    private IVideoService iVideoService;

    public Call<Void> createVideo(String token,Video video) {
        iVideoService = DataService.getVideoService();
        Call<Void> call = iVideoService.createVideo(token,video);
        try{
            call.execute().body();
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public List<VideoDTO> getVideosByDate() {
        iVideoService = DataService.getVideoService();
        Call<List<VideoDTO>> call = iVideoService.getVideosByDate();
        List<VideoDTO> videoDTOS = null;
        try{
            videoDTOS = call.execute().body();
        } catch (Exception e){
            e.printStackTrace();
        }
        return videoDTOS;
    }
}
