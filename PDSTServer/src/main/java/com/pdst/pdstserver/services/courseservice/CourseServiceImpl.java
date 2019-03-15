package com.pdst.pdstserver.services.courseservice;


import com.pdst.pdstserver.dtos.CourseDTO;
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

    @Override
    public List<Course> getAllCourseByAccountId(int id) {
        return courseRepository.findAllByAccountId(id);
    }

    @Override
    public boolean createCourse(Course course) {
        Course resCourse = courseRepository.save(course);
        if (resCourse != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<Course> getAllCourseOrderByCreatedTime() {
        return courseRepository.findAllByOrderByCreatedTimeDesc();
    }

    @Override
    public List<Course> getAllCoursesWithPriceByAccountId(int accountId) {
        return courseRepository.getAllCoursesWithPriceByAccountId(accountId);
    }

    @Override
    public int countAllCoursesByAccountId(int accountId) {
        return courseRepository.countAllCoursesByAccountId(accountId);
    }

}
