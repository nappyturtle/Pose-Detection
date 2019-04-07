package com.pdst.pdstserver.controller;

import com.pdst.pdstserver.utils.SearchUtil;
import com.pdst.pdstserver.model.Video;
import com.pdst.pdstserver.dto.VideoDTO;
import com.pdst.pdstserver.dto.VideoDTOFrontEnd;
import com.pdst.pdstserver.service.VideoService;
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

    /**
     @author  KietPT
     @since   6/4/2019

     - api này dùng để video detail
     - dùng cho web
     */
    @GetMapping("getVideoDetailById/{videoId}")
    public VideoDTOFrontEnd getVideoDetailById(@PathVariable(value = "videoId") int videoId) {
        return videoService.getVideoDetailById(videoId);
    }

    /**
     @author  KietPT
     @since   6/4/2019

     - api này dùng để tăng số lượt xem cho video
     - dùng cho mobile
     */
    @PutMapping("changeNumberOfViewByVideoId/{videoId}")
    public boolean changeNumberOfViewByVideoId(@PathVariable(value = "videoId") int videoId) {
        return videoService.changeNumberOfViewByVideoId(videoId);
    }

    /**
     @author  KietPT
     @since   6/4/2019

     - api này dùng để lấy danh sách video
     - dùng cho web
     */
    @GetMapping("getAllVideoByStaffOrAdmin")
    public List<VideoDTOFrontEnd> getAllVideoByStaffOrAdmin() {
        return videoService.getAllVideoByStaffOrAdmin();
    }

    /**
     @author  KietPT
     @since   6/4/2019

     - api này dùng để edit video
     - dùng cho web
     */
    @PutMapping("editVideoStatusByStaffOrAdmin")
    public boolean editVideoStatusByStaffOrAdmin(@RequestBody VideoDTOFrontEnd dto) {
        return videoService.editVideoStatusByStaffOrAdmin(dto);
    }

    /**
     @author  KietPT
     @since   6/4/2019

     - api này dùng để lấy danh sách video free, sort theo createTime giảm dần, cho trang home
     - dùng cho mobile
     */
    @GetMapping("getAllVideosByDate")
    public List<VideoDTO> getAllVideoByDate(@RequestParam(value = "page") int page,
                                            @RequestParam(value = "size") int size) {
        List<VideoDTO> resultPage = videoService.getAllVideosOrderByDate(page, size);
        return resultPage;
    }

    /**
     @author  KietPT
     @since   6/4/2019

     - api này dùng để lấy danh sách video liên quan( màn hình PlayVideoActivity, list video related bên dưới)
     - lấy top 6 video free
     - dùng cho mobile
     */
    @GetMapping("getAllVideosRelatedByCourseId")
    public List<VideoDTO> getAllVideosRelatedByCourseId(@RequestParam(value = "courseId") int courseId,
                                               @RequestParam(value = "currentVideoId") int currentVideoId) {
        List<VideoDTO> resultPage = videoService.getAllVideosRelatedByCourseId(courseId,currentVideoId);
        return resultPage;
    }

    /**
     @author  KietPT
     @since   6/4/2019

     - api này dùng để lấy danh sách video free, sort by só lượt xem
     - dùng cho mobile
     */
    @GetMapping("getAllVideosByTopNumOfView")
    public List<VideoDTO> getAllVideosByTopNumOfView(@RequestParam(value = "page") int page,
                                                     @RequestParam(value = "size") int size) {
        List<VideoDTO> resultPage = videoService.getAllVideosByTopNumOfView(page, size);
        return resultPage;
    }


    @GetMapping("/getVideo")
    public Optional<Video> getVideoById(@RequestParam(value = "id") Integer id) {
        return videoService.getVideoById(id);
    }

    /**
     @author  KietPT
     @since   6/4/2019

     - api này dùng để tạo video và send request đến service để tiến hành tạo dataset
     - dùng cho mobile
     */
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

    /**
     @author  KietPT
     @since   6/4/2019

     - api này dùng để lấy danh sách video đã mua
     - dùng cho mobile
     */
    @GetMapping("getAllBoughtVideosByCourseId")
    public List<VideoDTO> getAllBoughtVideosByCourseId(@RequestParam(value = "page") int page,
                                                       @RequestParam(value = "size") int size,
                                                       @RequestParam(value = "traineeId") int traineeId,
                                                       @RequestParam(value = "courseId") int courseId) {
        List<VideoDTO> resultPage = videoService.getAllBoughtVideosByCourseId(page, size, traineeId,courseId);
        return resultPage;
    }

    /**
     @author  KietPT
     @since   6/4/2019

     - api này dùng để lấy danh sách video đã chưa mua(trainer cập nhật thêm những video mới)
     - dùng cho mobile
     */
    @GetMapping("getAllUnBoughtVideoByCourseId")
    public List<VideoDTO> getAllUnBoughtVideoByCourseId(@RequestParam(value = "page") int page,
                                                        @RequestParam(value = "size") int size,
                                                        @RequestParam(value = "traineeId") int traineeId,
                                                        @RequestParam(value = "courseId") int courseId) {
        List<VideoDTO> resultPage = videoService.getAllUnBoughtVideoByCourseId(page, size, traineeId,courseId);
        return resultPage;
    }

    /**
     @author  KietPT
     @since   6/4/2019

     - api này dùng để lấy danh sách video để edit
     - dùng cho mobile
     */
    @GetMapping("getAllVideoByCourseIdToEdit")
    public List<VideoDTO> getAllVideoByCourseIdToEdit(@RequestParam(value = "courseId") int courseId) {
        List<VideoDTO> resultPage = videoService.getAllVideoByCourseIdToEdit(courseId);
        return resultPage;
    }

    /**
     @author  KietPT
     @since   6/4/2019

     - api này dùng để lấy danh sách video đã mua
     - dùng cho mobile
     */
    @GetMapping("getAllBoughtVideoRelated")
    public List<VideoDTO> getAllBoughtVideoRelated(@RequestParam(value = "traineeId") int traineeId,
                                                       @RequestParam(value = "courseId") int courseId,
                                                   @RequestParam(value = "videoId") int videoId) {
        List<VideoDTO> resultPage = videoService.getAllBoughtVideoRelated(traineeId,courseId, videoId);
        return resultPage;
    }

    /**
     @author  KietPT
     @since   6/4/2019

     - api này dùng để search video cho trang home
     - dùng cho mobile
     */
    @GetMapping("searchOrderByDate")
    public List<VideoDTO> searchVideoOrderByDate(@RequestParam(value = "searchValue") String searchValue) {
        List<VideoDTO> videoDTOList = searchVideo(searchValue);
        Collections.sort(videoDTOList, (videoDTO, t1) -> {return t1.getVideo().getCreatedTime().compareTo(videoDTO.getVideo().getCreatedTime());});
        return videoDTOList;
    }

    /**
     @author  KietPT
     @since   6/4/2019

     - api này dùng để search video cho trang thịnh hành
     - dùng cho mobile
     */
    @GetMapping("searchOrderByView")
    public List<VideoDTO> searchVideoOrderByView(@RequestParam(value = "searchValue") String searchValue) {
        List<VideoDTO> videoDTOList = searchVideo(searchValue);
        Collections.sort(videoDTOList, (videoDTO, t1) -> {return t1.getVideo().getNumOfView().compareTo(videoDTO.getVideo().getNumOfView());});
        return videoDTOList;
    }

    /**
     @author  KietPT
     @since   6/4/2019

     -
     */
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

    /**
     @author  KietPT
     @since   6/4/2019

     - hàm này dùng để edit video
     - dùng cho mobile
     */
    @PutMapping(value = "editVideo")
    public boolean editVideo(@RequestBody Video video){
        return videoService.editVideo(video);
    }

}
