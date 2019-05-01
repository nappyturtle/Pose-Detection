package com.pdst.pdstserver.impl;

import com.pdst.pdstserver.model.Frame;
import com.pdst.pdstserver.repository.FrameRepository;
import com.pdst.pdstserver.service.FrameService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FrameServiceImpl implements FrameService {
    private final FrameRepository frameRepository;

    public FrameServiceImpl(FrameRepository frameRepository) {
        this.frameRepository = frameRepository;
    }

    @Override
    public Frame createFrame(Frame frame) {
        return frameRepository.save(frame);
    }

    @Override
    public List<Frame> getAllFramesByVideoId(int videoId) {
        return frameRepository.getAllByVideoIdEquals(videoId);
    }


}
