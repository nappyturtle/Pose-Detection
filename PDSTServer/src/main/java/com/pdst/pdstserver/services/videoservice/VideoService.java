package com.pdst.pdstserver.services.videoservice;

import com.pdst.pdstserver.models.Video;

import java.util.List;

public interface VideoService {
    List<Video> getAllVideos();
    List<Video> getAllVideosOrderByDate();
}
