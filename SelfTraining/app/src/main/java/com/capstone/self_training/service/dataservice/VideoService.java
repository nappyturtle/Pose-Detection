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

    public List<VideoDTO> getVideosByDate(int page, int size) {
        iVideoService = DataService.getVideoService();
        Call<List<VideoDTO>> call = iVideoService.getVideosByDate(page,size);
        List<VideoDTO> videoDTOS = null;
        try{
            videoDTOS = call.execute().body();
        } catch (Exception e){
            e.printStackTrace();
        }
        return videoDTOS;
    }

    public List<VideoDTO> getVideosByTrainer(int id) {
        iVideoService = DataService.getVideoService();
        Call<List<VideoDTO>> call = iVideoService.getVideosByTrainer(id);
        List<VideoDTO> videoDTOS = null;
        try{
            videoDTOS = call.execute().body();
        } catch (Exception e){
            e.printStackTrace();
        }
        return videoDTOS;
    }
    public List<VideoDTO> getAllVideosByTopNumOfView(int page, int size) {
        iVideoService = DataService.getVideoService();
        Call<List<VideoDTO>> call = iVideoService.getAllVideosByTopNumOfView(page,size);
        List<VideoDTO> videoDTOS = null;
        try{
            videoDTOS = call.execute().body();
        } catch (Exception e){
            e.printStackTrace();
        }
        return videoDTOS;
    }

    public List<VideoDTO> getAllBoughtVideosByCourseId(String token, int page, int size, int courseId) {
        iVideoService = DataService.getVideoService();
        Call<List<VideoDTO>> call = iVideoService.getAllBoughtVideosByCourseId(token,page,size,courseId);
        List<VideoDTO> videoDTOS = null;
        try{
            videoDTOS = call.execute().body();
        } catch (Exception e){
            e.printStackTrace();
        }
        return videoDTOS;
    }

    public List<VideoDTO> getAllBoughtVideoRelated(String token,int courseId, int videoId) {
        iVideoService = DataService.getVideoService();
        Call<List<VideoDTO>> call = iVideoService.getAllBoughtVideoRelated(token,courseId,videoId);
        List<VideoDTO> videoDTOS = null;
        try{
            videoDTOS = call.execute().body();
        } catch (Exception e){
            e.printStackTrace();
        }
        return videoDTOS;
    }
}
