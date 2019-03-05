package com.pdst.pdstserver.services.videoservice;

import com.pdst.pdstserver.dtos.VideoDTO;
import com.pdst.pdstserver.handlers.SendRequest;
import com.pdst.pdstserver.models.Account;
import com.pdst.pdstserver.models.Course;
import com.pdst.pdstserver.models.Video;
import com.pdst.pdstserver.repositories.AccountRepository;
import com.pdst.pdstserver.repositories.CourseRepository;
import com.pdst.pdstserver.repositories.VideoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VideoServiceImpl implements VideoService {

    private final VideoRepository videoRepository;
    private final AccountRepository accountRepository;
    private final CourseRepository courseRepository;

    public VideoServiceImpl(VideoRepository videoRepository, AccountRepository accountRepository, CourseRepository courseRepository) {
        this.videoRepository = videoRepository;
        this.accountRepository = accountRepository;
        this.courseRepository = courseRepository;
    }


    @Override
    public List<Video> getAllVideos() {
        return videoRepository.findAll();
    }

    @Override
   public List<VideoDTO> getAllVideosOrderByDate(int page, int size) {
        System.out.println("page - size = " + page + " - "+size);
        Page<Video> videos =  videoRepository.findAll(new PageRequest(page,size,Sort.Direction.DESC,"createdTime"));

        List<VideoDTO> dtos = new ArrayList<>();
        for (Video video : videos) {
            Course course = courseRepository.findCourseById(video.getCourseId());
//            Account account = accountRepository.findAccountById(video.getAccountId());
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
    public Optional<Video> getVideoById(int id) {
        return Optional.empty();
    }


    @Override
    public boolean createVideo(Video video) {
        // lưu video vào db
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

    }

    //cho nay chua biet sua sao, tam thoi comment lai - VuVG 03/05/2019
    @Override
    public List<VideoDTO> getAllVideosByTrainer(int accountId) {
//        List<Video> videos = videoRepository.findAllByAccountIdOrderByCreatedTimeDesc(accountId);
//        List<VideoDTO> dtos = new ArrayList<>();
//        for (Video video : videos) {
//            Account account = accountRepository.findAccountById(video.getAccountId());
//            VideoDTO dto = new VideoDTO();
//            dto.setVideo(video);
//            dto.setUsername(account.getUsername());
//            dto.setImgUrl(account.getImgUrl());
//            dtos.add(dto);
//    }
//        return dtos;
        return null;
    }

    @Override
    public List<VideoDTO> getAllVideosByTopNumOfView(int page,int size) {
        System.out.println("page - size = " + page + " - "+size);
        Page<Video> videos =  videoRepository.findAll(new PageRequest(page,size,Sort.Direction.DESC,"numOfView"));

        List<VideoDTO> dtos = new ArrayList<>();
        for (Video video : videos) {
            Course course = courseRepository.findCourseById(video.getCourseId());
//            Account account = accountRepository.findAccountById(video.getAccountId());
            Account account = accountRepository.findAccountById(course.getAccountId());
            VideoDTO dto = new VideoDTO();
            dto.setVideo(video);
            dto.setUsername(account.getUsername());
            dto.setImgUrl(account.getImgUrl());
            dtos.add(dto);
        }
        return dtos;
    }
}
