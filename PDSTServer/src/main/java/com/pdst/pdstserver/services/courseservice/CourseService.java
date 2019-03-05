package com.pdst.pdstserver.services.courseservice;


import com.pdst.pdstserver.models.Course;
import com.pdst.pdstserver.models.Enrollment;

import java.util.List;

public interface CourseService {
    List<Course> getAllCourses();
}
