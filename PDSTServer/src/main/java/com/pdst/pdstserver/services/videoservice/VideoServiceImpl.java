package com.pdst.pdstserver.services.videoservice;

import com.pdst.pdstserver.models.Video;
import com.pdst.pdstserver.repositories.VideoRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
}
