package com.pdst.pdstserver.repositories;

import com.pdst.pdstserver.dtos.CourseDTO;
import com.pdst.pdstserver.models.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
    Course findCourseById(Integer id);

    List<Course> findAllByAccountId(int id);

    @Query(value = "select c, a.username from Course c left join Account a on c.accountId = a.id where c.price <> 0")
    List<CourseDTO> getAllCoursesWithPrice();

    List<Course> findAllByOrderByCreatedTimeDesc();
}
