package com.pdst.pdstserver.repositories;

import com.pdst.pdstserver.dtos.VideoDTO;
import com.pdst.pdstserver.models.Video;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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
//    List<Video> findAllByOrderByCreatedTimeDesc(Pageable pageable);
//    List<Video> findAllByOrderByNumOfViewDesc(Pageable pageable);

    @Query(value = "select v " +
            "from Video v join Course c " +
            "on v.courseId = c.id " +
            "where c.price = 0 "

    )
    List<Video> getAllVideoByCourseFree(Pageable pageable);

//        @Query(value = "SELECT v " +
//                "FROM Video v, Course c, Account a " +
//                "WHERE a.id = c.accountId AND v.courseId = c.id AND c.price = 0 " +
//                "order by v.createdTime desc"
//    )
//    List<Video> getAllVideoByCourseFree(Pageable pageable);

    @Query(value = "select count(v.id) from Video v")
    int countAllVideos();

    List<Video> findAllByOrderByCreatedTimeDesc();
}
