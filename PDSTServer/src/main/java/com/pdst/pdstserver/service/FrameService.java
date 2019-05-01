package com.pdst.pdstserver.service;


import com.pdst.pdstserver.model.Frame;

import java.util.List;

public interface FrameService {
    Frame createFrame(Frame frame);
    List<Frame> getAllFramesByVideoId(int videoId);
}
