package com.pdst.pdstserver.services.videoservice;

import com.pdst.pdstserver.models.Video;

import java.util.List;
import java.util.Optional;

public interface VideoService {
    List<Video> getAllVideos();
    List<Video> getAllVideosOrderByDate();
    Optional<Video> getVideoById(Integer id);
    Video createVideo(Video video);

}
