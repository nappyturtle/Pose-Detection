package com.pdst.pdstserver.course;


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
    CourseDTOFrontEnd getCourseDetailById(int courseId);
}
