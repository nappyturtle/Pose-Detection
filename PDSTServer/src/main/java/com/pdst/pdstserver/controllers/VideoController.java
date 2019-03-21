package com.pdst.pdstserver.controllers;

import com.pdst.pdstserver.dtos.VideoDTO;
import com.pdst.pdstserver.handlers.SearchUtil;
import com.pdst.pdstserver.models.Suggestion;
import com.pdst.pdstserver.models.Video;
import com.pdst.pdstserver.services.videoservice.VideoService;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.util.stream.Collectors;

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
    public List<VideoDTO> getAllVideoByDate(@RequestParam(value = "page") int page, @RequestParam(value = "size") int size) {
        List<VideoDTO> resultPage = videoService.getAllVideosOrderByDate(page, size);
        return resultPage;
    }

    @GetMapping("getAllVideosRelatedByCourseId")
    public List<VideoDTO> getAllVideoByTrainer(@RequestParam(value = "courseId") int courseId,
                                               @RequestParam(value = "currentVideoId") int currentVideoId) {
        List<VideoDTO> resultPage = videoService.getAllVideosRelatedByCourseId(courseId,currentVideoId);
        return resultPage;
    }

    @GetMapping("getAllVideosByTopNumOfView")
    public List<VideoDTO> getAllVideosByTopNumOfView(@RequestParam(value = "page") int page, @RequestParam(value = "size") int size) {
        List<VideoDTO> resultPage = videoService.getAllVideosByTopNumOfView(page, size);
        return resultPage;
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

    @GetMapping("getAllVideoByCourseId")
    public List<VideoDTO> getAllVideoByCourseId(@RequestParam(value = "page") int page, @RequestParam(value = "size") int size, @RequestParam(value = "courseId") int courseId) {
        List<VideoDTO> resultPage = videoService.getAllVideoByCourseId(page, size, courseId);
        return resultPage;
    }

    @GetMapping("getAllVideoByCourseIdToEdit")
    public List<VideoDTO> getAllVideoByCourseIdToEdit(@RequestParam(value = "courseId") int courseId) {
        List<VideoDTO> resultPage = videoService.getAllVideoByCourseIdToEdit(courseId);
        return resultPage;
    }

    @GetMapping("getAllBoughtVideoRelated")
    public List<VideoDTO> getAllBoughtVideoRelated(@RequestParam(value = "courseId") int courseId, @RequestParam(value = "videoId") int videoId) {
        List<VideoDTO> resultPage = videoService.getAllBoughtVideoRelated(courseId, videoId);
        return resultPage;
    }
    @GetMapping("searchOrderByDate")
    public List<VideoDTO> searchVideoOrderByDate(@RequestParam(value = "searchValue") String searchValue) {
        List<VideoDTO> videoDTOList = searchVideo(searchValue);
        Collections.sort(videoDTOList, (videoDTO, t1) -> {return t1.getVideo().getCreatedTime().compareTo(videoDTO.getVideo().getCreatedTime());});
        return videoDTOList;
    }
    @GetMapping("searchOrderByView")
    public List<VideoDTO> searchVideoOrderByView(@RequestParam(value = "searchValue") String searchValue) {
        List<VideoDTO> videoDTOList = searchVideo(searchValue);
        Collections.sort(videoDTOList, (videoDTO, t1) -> {return t1.getVideo().getNumOfView().compareTo(videoDTO.getVideo().getNumOfView());});
        return videoDTOList;
    }

    @GetMapping("getAllFreeVideosByAccount")
    public List<Video> getAllFreeVideosByAccount(@RequestParam(value = "accountId")int accountId){
        List<Video> videos = videoService.getAllFreeVideosByAccount(accountId);
        return videos;
    }
    private List<VideoDTO> searchVideo(String searchValue) {
        List<VideoDTO> videoDTOList = videoService.getAllFreeVideos();
        Map<VideoDTO, Double> videoMap = new HashMap<>();
        String[] tokens = searchValue.toLowerCase().split(" ");
        for (VideoDTO dto : videoDTOList) {
            videoMap.put(dto, SearchUtil.searchMatchingPercentage(tokens, dto.getVideo().getTitle()));
        }
        videoMap = videoMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));
        Iterator iterator = videoMap.keySet().iterator();
        while (iterator.hasNext()) {
            VideoDTO videoDTO = (VideoDTO) iterator.next();
            if (videoMap.get(videoDTO) < 0.5) {
                videoDTOList.remove(videoDTO);
            }
        }
        return videoDTOList;
    }
    @PutMapping(value = "editVideo")
    public boolean editVideo(@RequestBody Video video){
        return videoService.editVideo(video);
    }

}
