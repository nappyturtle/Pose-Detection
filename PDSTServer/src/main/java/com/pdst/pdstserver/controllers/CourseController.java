package com.pdst.pdstserver.controllers;

import com.pdst.pdstserver.models.Course;
import com.pdst.pdstserver.services.courseservice.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("getAllCoursesByAccountId")
    public List<Course> getAllCoursesByAccountId(@RequestParam(value = "accountId") int id) {
        return courseService.getAllCourseByAccountId(id);
    }

    @PostMapping("create")
    public ResponseEntity<Void> createCourse(@RequestBody Course course) {
        boolean result = courseService.createCourse(course);
        if (result) {
            return new ResponseEntity<Void>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

}
