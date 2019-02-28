package com.pdst.pdstserver.controllers;

import com.pdst.pdstserver.models.VideoTransaction;
import com.pdst.pdstserver.services.videotransactionservice.VideoTransactionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(VideoTransactionController.BASE_URL)
public class VideoTransactionController {
    public static final String BASE_URL = "transaction";

    private final VideoTransactionService videoTransactionService;

    public VideoTransactionController(VideoTransactionService videoTransactionService) {
        this.videoTransactionService = videoTransactionService;
    }
    @GetMapping("transactions")
    public List<VideoTransaction> getAllTransactions() {
        return videoTransactionService.getAllTransaction();
    }
}
