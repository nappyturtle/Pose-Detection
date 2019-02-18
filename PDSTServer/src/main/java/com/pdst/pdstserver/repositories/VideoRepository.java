package com.pdst.pdstserver.repositories;

import com.pdst.pdstserver.models.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRepository extends JpaRepository<Video, Integer> {
    Video findVideoById(Integer id);
}
