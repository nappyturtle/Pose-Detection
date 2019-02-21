package com.pdst.pdstserver.controllers;

import com.pdst.pdstserver.dtos.VideoDTO;
import com.pdst.pdstserver.models.Suggestion;
import com.pdst.pdstserver.models.Video;
import com.pdst.pdstserver.services.videoservice.VideoService;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

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
    public List<Video> getAllVideos() {
        return videoService.getAllVideos();
    }

    @GetMapping("getAllVideosByDate")
    public List<VideoDTO> getAllVideoByDate() {
        return videoService.getAllVideosOrderByDate();

    }


    @GetMapping("/getVideo")
    public Optional<Video> getVideoById(@RequestParam(value = "id") Integer id) {
        return videoService.getVideoById(id);
    }

    @PostMapping(value = "/create")
    public ResponseEntity<Void> createVideo(@RequestBody Video video, UriComponentsBuilder builder) {
        boolean flag = videoService.createVideo(video);
        if (flag == false) {
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.path("create/{title}").buildAndExpand(video.getTitle()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }


}
