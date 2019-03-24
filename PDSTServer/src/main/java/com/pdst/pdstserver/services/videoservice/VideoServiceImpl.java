package com.pdst.pdstserver.services.videoservice;

import com.pdst.pdstserver.dtos.VideoDTO;
import com.pdst.pdstserver.dtos.VideoDTOFrontEnd;
import com.pdst.pdstserver.handlers.SendRequest;
import com.pdst.pdstserver.models.Account;
import com.pdst.pdstserver.models.Course;
import com.pdst.pdstserver.models.Video;
import com.pdst.pdstserver.repositories.AccountRepository;
import com.pdst.pdstserver.repositories.CourseRepository;
import com.pdst.pdstserver.repositories.VideoRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

import static java.util.Comparator.comparing;

@Service
public class VideoServiceImpl implements VideoService {

    private final VideoRepository videoRepository;
    private final AccountRepository accountRepository;
    private final CourseRepository courseRepository;

    public VideoServiceImpl(VideoRepository videoRepository, AccountRepository accountRepository,
                            CourseRepository courseRepository) {
        this.videoRepository = videoRepository;
        this.accountRepository = accountRepository;
        this.courseRepository = courseRepository;
    }


    @Override
    public List<Video> getAllVideos() {
        List<Video> listTemp = videoRepository.findAll();
        List<Video> videos = new ArrayList<>();
        for(Video video : listTemp){
            Course course = courseRepository.getOne(video.getCourseId());
            video.setFolderName(course.getName());
            videos.add(video);
        }
        return videos;
    }

    @Override
    public List<VideoDTO> getAllVideosOrderByDate(int page, int size) {
        System.out.println("page - size = " + page + " - " + size);

        List<Video> videos = videoRepository.getAllVideoByCourseFree(PageRequest.of(page,size,Sort.by("createdTime").descending()));
        List<VideoDTO> dtos = new ArrayList<>();

        for (Video video : videos) {
            Course course = courseRepository.findCourseById(video.getCourseId());
            //VuVG - 15/03/2019 - Chỉ lấy các video free
            //if(course.getPrice() == 0) {
                Account account = accountRepository.findAccountById(course.getAccountId());
                VideoDTO dto = new VideoDTO();
                dto.setVideo(video);
                dto.setUsername(account.getUsername());
                dto.setImgUrl(account.getImgUrl());
                dto.setAccountId(account.getId());
                dtos.add(dto);
            //}
        }
        return dtos;
//

    }

    @Override
    public Optional<Video> getVideoById(int id) {
        return Optional.empty();
    }


    @Override
    public boolean createVideo(Video video) {
        // lưu video vào db
        try {
            Video videoRequest = videoRepository.save(video);
            // gửi request đến service để cắt video
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(3000);
                        SendRequest sendRequest = new SendRequest();
                        sendRequest.sendRequestToCreateDataset(videoRequest);
                        System.out.println("da goi request to create data set");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            t.start();
            System.out.println("da insert xong");
            //return videoRequest;
            if (videoRequest == null) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean editVideo(Video video) {
        Video videoTemp = videoRepository.findVideoById(video.getId());
        videoTemp.setTitle(video.getTitle());
        videoTemp.setThumnailUrl(video.getThumnailUrl());
        videoTemp.setCourseId(video.getCourseId());
        videoTemp.setUpdatedTime(video.getUpdatedTime());
        Video res = videoRepository.save(videoTemp);
        if (res != null) {
            return true;
        }
        return false;
    }

    //cho nay chua biet sua sao, tam thoi comment lai - VuVG 03/05/2019
    // đã sửa lại thành lấy những video liên quan đến course đó, nhưng ko lấy video hiện tại
    @Override
    public List<VideoDTO> getAllVideosRelatedByCourseId(int courseId, int currentVideoId) {
       List<Video> listTemp = videoRepository.findTop6ByCourseIdOrderByCreatedTimeDesc(courseId);

        List<VideoDTO> dtoList = new ArrayList<>();
        for (int i = 0; i < listTemp.size(); i++) {
            if (listTemp.get(i).getId() == currentVideoId) {
                listTemp.remove(i);
            }
        }
        for (Video video : listTemp) {
            System.out.println("da vao dây roi neeeeeeeee!!!!!");
            Course course = courseRepository.findCourseById(courseId);
            Account account = accountRepository.findAccountById(course.getAccountId());
            VideoDTO dto = new VideoDTO();
            dto.setVideo(video);
            dto.setUsername(account.getUsername());
            dto.setImgUrl(account.getImgUrl());
            dtoList.add(dto);
        }
        return dtoList;
    }

    @Override
    public List<VideoDTO> getAllVideoByCourseId(int page, int size, int courseId) {
        System.out.println("page - size = " + page + " - " + size);
        List<Video> videos = videoRepository.findAllByCourseIdOrderByCreatedTimeDesc(new PageRequest(page, size), courseId);
        List<VideoDTO> dtos = new ArrayList<>();
        for (Video video : videos) {
            Course course = courseRepository.findCourseById(video.getCourseId());
            Account account = accountRepository.findAccountById(course.getAccountId());
            VideoDTO dto = new VideoDTO();
            dto.setVideo(video);
            dto.setUsername(account.getUsername());
            dto.setImgUrl(account.getImgUrl());
            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    public List<VideoDTO> getAllVideoByCourseIdToEdit(int courseId) {
        List<Video> videos = videoRepository.findAllByCourseId(courseId);
        List<VideoDTO> dtos = new ArrayList<>();
        for (Video video : videos) {
            Course course = courseRepository.findCourseById(video.getCourseId());
            Account account = accountRepository.findAccountById(course.getAccountId());
            VideoDTO dto = new VideoDTO();
            dto.setVideo(video);
            dto.setUsername(account.getUsername());
            dto.setImgUrl(account.getImgUrl());
            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    public List<VideoDTO> getAllBoughtVideoRelated(int courseId, int videoId) {
        List<Video> listTemp = videoRepository.findTop6ByCourseIdOrderByCreatedTimeDesc(courseId);

        List<VideoDTO> dtoList = new ArrayList<>();
        for (int i = 0; i < listTemp.size(); i++) {
            if (listTemp.get(i).getId() == videoId) {
                listTemp.remove(i);
            }
        }
        for (Video video : listTemp) {
            System.out.println("da vao dây roi neeeeeeeee!!!!!");
            Course course = courseRepository.findCourseById(courseId);
            Account account = accountRepository.findAccountById(course.getAccountId());
            VideoDTO dto = new VideoDTO();
            dto.setVideo(video);
            dto.setUsername(account.getUsername());
            dto.setImgUrl(account.getImgUrl());
            dtoList.add(dto);
        }
        return dtoList;
    }

    @Override
    public int countVideosByCourseId(int courseId) {
        return videoRepository.countVideoByCourseId(courseId);
    }

    @Override
    public List<Video> getAllFreeVideosByAccount(int accountId) {
        int freeCourseId = -1;
        freeCourseId = courseRepository.getFreeCourseIdByAccountId(accountId);
        List<Video> list = new ArrayList<>();
        list = videoRepository.findAllByCourseIdOrderByCreatedTimeDesc(freeCourseId);
        return list;
    }

    @Override
    public List<VideoDTO> getAllVideosByTopNumOfView(int page, int size) {
        System.out.println("page - size = " + page + " - " + size);
        List<Video> videos = videoRepository.getAllVideoByCourseFree(PageRequest.of(page,size,Sort.by("numOfView").descending()));

        List<VideoDTO> dtos = new ArrayList<>();
        for (Video video : videos) {
            Course course = courseRepository.findCourseById(video.getCourseId());
//            Account account = accountRepository.findAccountById(video.getAccountId());
            //VuVG - 15/03/2019 - Chỉ lấy các video free
            //if (course.getPrice() == 0) {
                Account account = accountRepository.findAccountById(course.getAccountId());
                VideoDTO dto = new VideoDTO();
                dto.setVideo(video);
                dto.setUsername(account.getUsername());
                dto.setImgUrl(account.getImgUrl());
                dto.setAccountId(account.getId());
                dtos.add(dto);
            //}
        }
        return dtos;
    }

    @Override
    public List<VideoDTO> getAllFreeVideos() {
        List<Video> videos = videoRepository.findAll();
        List<VideoDTO> dtos = new ArrayList<>();
        for (Video video : videos) {
            Course course = courseRepository.findCourseById(video.getCourseId());
            if (course.getPrice() == 0) {
                Account account = accountRepository.findAccountById(course.getAccountId());
                VideoDTO dto = new VideoDTO();
                dto.setVideo(video);
                dto.setUsername(account.getUsername());
                dto.setImgUrl(account.getImgUrl());
                dtos.add(dto);
            }
        }
        return dtos;
    }

    @Override
    public List<VideoDTOFrontEnd> getAllVideoByStaffOrAdmin() {
        List<Video> listTemp = videoRepository.findAll();
        List<VideoDTOFrontEnd> listVideoDTOFrontEnd = new ArrayList<>();


        for(Video video : listTemp){
            VideoDTOFrontEnd videoDTOFrontEnd = new VideoDTOFrontEnd();
            Course course = courseRepository.getOne(video.getCourseId());
            videoDTOFrontEnd.setId(video.getId());
            videoDTOFrontEnd.setTitle(video.getTitle());
            videoDTOFrontEnd.setThumnailUrl(video.getThumnailUrl());
            videoDTOFrontEnd.setContentUrl(video.getContentUrl());
            videoDTOFrontEnd.setNumOfView(video.getNumOfView());
            videoDTOFrontEnd.setStatus(video.getStatus());
            videoDTOFrontEnd.setCourseName(course.getName());
            listVideoDTOFrontEnd.add(videoDTOFrontEnd);
        }
        return listVideoDTOFrontEnd;
    }

    @Override
    public boolean editVideoStatusByStaffOrAdmin(int id, String status) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = Calendar.getInstance().getTime();
        System.out.println("id = "+id);
        Video video = videoRepository.findVideoById(id);
        video.setStatus(status);
        video.setUpdatedTime(sdf.format(date));
        Video videRes = videoRepository.save(video);
        if(videRes != null){
            return true;
        }
        return false;
    }

    @Override
    public int countAllVideos() {
        return videoRepository.countAllVideos();
    }
}
