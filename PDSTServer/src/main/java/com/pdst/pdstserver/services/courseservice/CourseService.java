package com.pdst.pdstserver.services.courseservice;


import com.pdst.pdstserver.dtos.CourseDTO;
import com.pdst.pdstserver.dtos.CourseDTOFrontEnd;
import com.pdst.pdstserver.models.Course;
import com.pdst.pdstserver.models.Enrollment;

import java.util.List;

public interface CourseService {
    List<Course> getAllCourses();
    List<Course> getAllCourseByAccountId(int id);
    List<CourseDTO> getAllCourseByTrainerId(int page, int size, int id);
    boolean createCourse(Course course);
    boolean editCourse(Course course);
    List<Course> getAllCourseOrderByCreatedTime();
    List<Course> getAllCoursesWithPriceByAccountId(int accountId);

    int countAllCoursesByAccountId(int accountId);
    List<CourseDTOFrontEnd> getAllCourseByStaffOrAdmin();
    boolean editCourseByStaffOrAdmin(CourseDTOFrontEnd dto);
    int countAllCourses();
}
