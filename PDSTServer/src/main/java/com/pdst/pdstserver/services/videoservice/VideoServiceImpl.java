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
        return videoRepository.findAll(new Sort(Sort.Direction.DESC,"createdTime"));
    }


    public Optional<Video> getVideoById(Integer id) {
        return videoRepository.findById(id);
    }

    @Override
    public Video createVideo(Video video) {
//        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("NewPersistenceUnit");
//        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Video videoRequest = videoRepository.save(video);
//
//        entityManager.getTransaction().begin();
//        entityManager.persist(videoRequest);
//        entityManager.getTransaction().commit();

       SendRequest sendRequest = new SendRequest();
        sendRequest.sendRequestToCreateDataset(videoRequest);

        return videoRequest;


    }
}
