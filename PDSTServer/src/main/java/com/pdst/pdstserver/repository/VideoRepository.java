package com.pdst.pdstserver.repository;

import com.pdst.pdstserver.model.Video;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video, Integer>{
    Video findVideoById(Integer id);
//    List<Video> findAllByAccountIdOrderByCreatedTimeDesc(int accountId);
    List<Video> findAllByCourseIdAndStatus(Pageable pageable,Integer courseId,String status);
    List<Video> findAllByCourseIdAndStatus(Integer courseId, String status);
    List<Video> findAllByCourseId(Integer courseId);
    Integer countVideoByCourseId(Integer courseId);
    Integer countVideoByCourseIdAndStatus(int courseId,String status);

    List<Video> findTop6ByCourseIdAndStatusOrderByCreatedTimeDesc(int courseId, String status);

    List<Video> findAllByCourseIdAndStatusOrderByCreatedTimeDesc(int courseId,String status);


    @Query(value = "select v " +
            "from Video v join Course c " +
            "on v.courseId = c.id " +
            "where c.price = 0 and v.status = 'active'"
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
    List<Video> findAllByCreatedTimeGreaterThanAndCourseId(String enrollmentCreatedTime,int courseId);
    List<Video> findAllByCreatedTimeLessThanAndCourseId(Pageable pageable,String enrollmentCreatedTime,int courseId);

    List<Video> findTop6ByCreatedTimeLessThanAndCourseIdOrderByCreatedTimeDesc(String enrollmentCreatedTime,int courseId);
}
