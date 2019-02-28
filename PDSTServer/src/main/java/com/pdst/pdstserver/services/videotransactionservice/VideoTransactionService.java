package com.pdst.pdstserver.services.videotransactionservice;


import com.pdst.pdstserver.models.VideoTransaction;

import java.util.List;

public interface VideoTransactionService {
    List<VideoTransaction> getAllTransaction();
}
