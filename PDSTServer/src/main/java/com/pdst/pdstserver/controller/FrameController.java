package com.pdst.pdstserver.controller;

import com.pdst.pdstserver.model.Frame;
import com.pdst.pdstserver.service.FrameService;
import com.pdst.pdstserver.service.VideoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(FrameController.BASE_URL)
public class FrameController {
    public static final String BASE_URL = "frame";
    private final FrameService frameService;
    private final VideoService videoService;

    public FrameController(FrameService frameService, VideoService videoService) {
        this.frameService = frameService;
        this.videoService = videoService;
    }
    @PostMapping("createDataset")
    public String createDataset(@RequestBody List<Frame> frameList) {
        for(int i = 0; i < frameList.size(); i++) {
            Frame frame = frameList.get(i);
            frameService.createFrame(frame);
        }
        return "Created Dataset";
    }
    @PostMapping("createFrame")
    public Frame createFrame(@RequestBody Frame frame) {
        return frameService.createFrame(frame);
    }
    @GetMapping("getFramesByVideoId")
    public @ResponseBody List<Frame> getFramesByVideoId(@RequestParam(value = "videoId") int videoId) {
        return frameService.getAllFramesByVideoId(videoId);
    }
}
