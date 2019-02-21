package com.pdst.pdstserver.services.videoservice;

import com.pdst.pdstserver.dtos.VideoDTO;
import com.pdst.pdstserver.models.Video;

import java.util.List;
import java.util.Optional;

public interface VideoService {
    List<Video> getAllVideos();
    List<VideoDTO> getAllVideosOrderByDate();
    Optional<Video> getVideoById(int id);
    boolean createVideo(Video video);

}
