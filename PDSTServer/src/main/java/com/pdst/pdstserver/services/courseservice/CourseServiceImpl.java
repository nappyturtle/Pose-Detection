package com.pdst.pdstserver.services.courseservice;


import com.pdst.pdstserver.models.Course;
import com.pdst.pdstserver.models.Enrollment;
import com.pdst.pdstserver.repositories.CourseRepository;
import com.pdst.pdstserver.repositories.EnrollmentRepository;
import com.pdst.pdstserver.services.enrollmentservice.EnrollmentService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;


    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }
}
