package com.pdst.pdstserver.services.videotransactionservice;


import com.pdst.pdstserver.models.VideoTransaction;
import com.pdst.pdstserver.repositories.VideoTransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class VideoVideoTransactionServiceImpl implements VideoTransactionService {
    private final VideoTransactionRepository videoTransactionRepository;


    public VideoVideoTransactionServiceImpl(VideoTransactionRepository videoTransactionRepository) {
        this.videoTransactionRepository = videoTransactionRepository;
    }

    @Override
    public List<VideoTransaction> getAllTransaction() {
        return videoTransactionRepository.findAll();
    }
}
