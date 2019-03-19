package com.pdst.pdstserver.repositories;

import com.pdst.pdstserver.dtos.CourseDTO;
import com.pdst.pdstserver.models.Course;
import com.pdst.pdstserver.models.Enrollment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
    Course findCourseById(Integer id);

    List<Course> findAllByAccountId(int id);

    @Query(value = "select c, a.username from Course c left join Account a on c.accountId = a.id where c.price <> 0")
    List<CourseDTO> getAllCoursesWithPrice();

    List<Course> findAllByOrderByCreatedTimeDesc();

    @Query("SELECT c.id FROM Course c WHERE c.accountId = ?1 and c.price = 0")
    int getFreeCourseIdByAccountId(int accountId);

    @Query("SELECT c FROM Course c WHERE c.accountId = ?1 AND c.price <> 0 order by c.createdTime desc ")
    List<Course> getAllCoursesWithPriceByAccountId(int accountId);

    @Query("SELECT COUNT (c.id) FROM Course c WHERE c.accountId = ?1 AND c.price > 0")
    int countAllCoursesByAccountId(int accountId);

    List<Course> findAllByAccountIdOrderByCreatedTimeDesc(Pageable pageable, int accountId);
}
