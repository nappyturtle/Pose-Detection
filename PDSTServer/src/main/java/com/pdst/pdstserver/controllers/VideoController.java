package com.pdst.pdstserver.controllers;

import com.pdst.pdstserver.models.Video;
import com.pdst.pdstserver.services.videoservice.VideoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(VideoController.BASE_URL)
public class VideoController {
    public static final String BASE_URL = "video";
    private final VideoService videoService;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @GetMapping("getAllVideos")
    public List<Video> getAllVideos(){
        return videoService.getAllVideos();
    }

    @GetMapping("getAllVideosByDate")
    public List<Video> getAllVideoByDate(){
        return videoService.getAllVideosOrderByDate();
    }


    @GetMapping("getVideo")
    public Optional<Video> getVideoById(@RequestParam(value = "id") Integer id){
        return videoService.getVideoById(id);
    }
}
