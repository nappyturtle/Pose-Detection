package com.pdst.pdstserver.controllers;

import com.pdst.pdstserver.models.Course;
import com.pdst.pdstserver.services.courseservice.CourseService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(CourseController.BASE_URL)
public class CourseController {
    public static final String BASE_URL = "course";
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }
    @GetMapping("courses")
    public List<Course> getAllEnrollments() {
        return courseService.getAllCourses();
    }

}
