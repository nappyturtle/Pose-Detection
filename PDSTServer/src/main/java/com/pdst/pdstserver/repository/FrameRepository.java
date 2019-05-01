package com.pdst.pdstserver.repository;

import com.pdst.pdstserver.model.Frame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FrameRepository extends JpaRepository<Frame, Integer> {
    List<Frame> getAllByVideoIdEquals(int videoId);
}
