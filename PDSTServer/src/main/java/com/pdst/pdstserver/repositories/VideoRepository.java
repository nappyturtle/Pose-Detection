package com.pdst.pdstserver.repositories;

import com.pdst.pdstserver.models.Video;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video, Integer>{
    Video findVideoById(Integer id);
//    List<Video> findAllByAccountIdOrderByCreatedTimeDesc(int accountId);
    List<Video> findAllByCourseIdOrderByCreatedTimeDesc(Pageable pageable,Integer courseId);
    List<Video> findAllByCourseId(Integer courseId);
    Integer countVideoByCourseId(Integer courseId);
    Integer countVideoByCourseId(int courseId);
    List<Video> findTop6ByCourseIdOrderByCreatedTimeDesc(int courseId);
    List<Video> findAllByCourseIdOrderByCreatedTimeDesc(int courseId);
    List<Video> findAllByOrderByCreatedTimeDesc(Pageable pageable);
    List<Video> findAllByOrderByNumOfViewDesc(Pageable pageable);
}
