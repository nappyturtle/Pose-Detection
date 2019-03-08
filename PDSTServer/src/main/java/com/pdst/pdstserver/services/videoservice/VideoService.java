package com.pdst.pdstserver.services.videoservice;

import com.pdst.pdstserver.dtos.VideoDTO;
import com.pdst.pdstserver.models.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface VideoService {
    List<Video> getAllVideos();
    List<VideoDTO> getAllVideosOrderByDate(int page, int size);
    List<VideoDTO> getAllVideosByTopNumOfView(int page,int size);
    Optional<Video> getVideoById(int id);
    boolean createVideo(Video video);
    List<VideoDTO> getAllVideosByTrainer(int accountId);
    List<VideoDTO> getAllVideoByCourseId(int page, int size, int courseId);
    List<VideoDTO> getAllBoughtVideoRelated(int courseId, int videoId);
}
