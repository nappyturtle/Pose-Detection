package com.pdst.pdstserver.services.videoservice;

import com.pdst.pdstserver.handlers.SendRequest;
import com.pdst.pdstserver.models.Video;
import com.pdst.pdstserver.repositories.VideoRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Optional;

@Service
public class VideoServiceImpl implements VideoService {

    private final VideoRepository videoRepository;

    public VideoServiceImpl(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    @Override
    public List<Video> getAllVideos() {
        return videoRepository.findAll();
    }

    @Override
    public List<Video> getAllVideosOrderByDate() {
        return videoRepository.findAll(new Sort(Sort.Direction.DESC, "createdTime"));
    }


    public Optional<Video> getVideoById(Integer id) {
        return videoRepository.findById(id);
    }

    @Override
    public Video createVideo(Video video) {
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
        return videoRequest;


    }
}
