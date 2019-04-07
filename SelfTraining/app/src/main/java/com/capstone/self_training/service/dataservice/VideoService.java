package com.capstone.self_training.service.dataservice;

import android.util.Log;

import com.capstone.self_training.dto.VideoDTO;
import com.capstone.self_training.model.Video;
import com.capstone.self_training.service.iService.IVideoService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class VideoService {

    private IVideoService iVideoService;

    public Call<Void> createVideo(String token, Video video) {
        iVideoService = DataService.getVideoService();
        Call<Void> call = iVideoService.createVideo(token, video);
        try {
            call.execute().body();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean editVideo(String token, Video video) {
        iVideoService = DataService.getVideoService();
        Call<Boolean> call = iVideoService.editVideo(token, video);
        boolean response = false;
        try {
            response = call.execute().body();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public boolean changeNumberOfViewByVideoId(String token, int videoId) {
        iVideoService = DataService.getVideoService();
        Call<Boolean> call = iVideoService.changeNumberOfViewByVideoId(token, videoId);
        boolean response = false;
        try {
            response = call.execute().body();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public List<VideoDTO> getVideosByDate(int page, int size) {
        iVideoService = DataService.getVideoService();
        Call<List<VideoDTO>> call = iVideoService.getVideosByDate(page, size);
        List<VideoDTO> videoDTOS = null;
        try {
            videoDTOS = call.execute().body();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return videoDTOS;
    }

    public List<VideoDTO> getAllVideosRelatedByCourseId(int courseId,int currentVideoId) {
        iVideoService = DataService.getVideoService();
        Call<List<VideoDTO>> call = iVideoService.getAllVideosRelatedByCourseId(courseId,currentVideoId);
        List<VideoDTO> videoDTOS = null;
        try {
            videoDTOS = call.execute().body();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return videoDTOS;
    }

    public List<VideoDTO> getAllVideoByCourseIdToEdit(String token, int id) {
        iVideoService = DataService.getVideoService();
        Call<List<VideoDTO>> call = iVideoService.getAllVideoByCourseIdToEdit(token,id);
        List<VideoDTO> videoDTOS = null;
        try {
            videoDTOS = call.execute().body();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return videoDTOS;
    }

    public List<VideoDTO> getAllVideosByTopNumOfView(int page, int size) {
        iVideoService = DataService.getVideoService();
        Call<List<VideoDTO>> call = iVideoService.getAllVideosByTopNumOfView(page, size);
        List<VideoDTO> videoDTOS = null;
        try {
            videoDTOS = call.execute().body();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return videoDTOS;
    }

    public List<VideoDTO> getAllBoughtVideosByCourseId(String token, int page, int size, int traineeId,int courseId) {
        iVideoService = DataService.getVideoService();
        Call<List<VideoDTO>> call = iVideoService.getAllBoughtVideosByCourseId(token, page, size, traineeId,courseId);
        List<VideoDTO> videoDTOS = null;
        try {
            videoDTOS = call.execute().body();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return videoDTOS;
    }

    public List<VideoDTO> getAllUnBoughtVideoByCourseId(String token, int page, int size, int traineeId, int courseId) {
        iVideoService = DataService.getVideoService();
        Call<List<VideoDTO>> call = iVideoService.getAllUnBoughtVideoByCourseId(token, page, size, traineeId,courseId);
        List<VideoDTO> videoDTOS = null;
        try {
            videoDTOS = call.execute().body();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return videoDTOS;
    }

    public List<VideoDTO> getAllBoughtVideoRelated(String token, int traineeId,int courseId, int videoId) {
        iVideoService = DataService.getVideoService();
        Call<List<VideoDTO>> call = iVideoService.getAllBoughtVideoRelated(token, traineeId,courseId, videoId);
        List<VideoDTO> videoDTOS = null;
        try {
            videoDTOS = call.execute().body();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return videoDTOS;
    }

    public List<Video> getAllFreeVideosByAccount(int accountId) {
        iVideoService = DataService.getVideoService();
        Call<List<Video>> call = iVideoService.getAllFreeVideosByAccount(accountId);
        List<Video> videos = null;
        try {
            videos = call.execute().body();
        } catch (Exception e) {
            Log.e("VideoServie getAllFreeVideosByAccount: ", e.getMessage());
        }
        return videos;
    }

    public List<VideoDTO> searchVideoOrderByDate(String searchValue) {
        iVideoService = DataService.getVideoService();
        List<VideoDTO> videos = null;
        Call<List<VideoDTO>> call = iVideoService.searchVideoOrderByDate(searchValue);
        try {
            videos = call.execute().body();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return videos;
    }
    public List<VideoDTO> searchVideoOrderByView(String searchValue) {
        iVideoService = DataService.getVideoService();
        List<VideoDTO> videos = null;
        Call<List<VideoDTO>> call = iVideoService.searchVideoOrderByView(searchValue);
        try {
            videos = call.execute().body();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return videos;
    }
}
