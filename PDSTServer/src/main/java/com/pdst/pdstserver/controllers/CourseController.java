package com.pdst.pdstserver.controllers;

import com.pdst.pdstserver.dtos.CourseDTO;
import com.pdst.pdstserver.models.Account;
import com.pdst.pdstserver.models.Course;
import com.pdst.pdstserver.services.accountservice.AccountService;
import com.pdst.pdstserver.services.courseservice.CourseService;
import com.pdst.pdstserver.services.videoservice.VideoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(CourseController.BASE_URL)
public class CourseController {
    public static final String BASE_URL = "course";
    private final CourseService courseService;
    private final AccountService accountService;
    private final VideoService videoService;

    public CourseController(CourseService courseService, AccountService accountService, VideoService videoService) {
        this.courseService = courseService;
        this.accountService = accountService;
        this.videoService = videoService;
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

    //get all courses which have price
    //number of regis not yet complete
    @GetMapping("getAllCourses")
    public List<CourseDTO> getAllCoursesWithPrice() {
        List<Course> courses = new ArrayList<>();
        List<CourseDTO> courseDTOS = new ArrayList<>();
        courses = courseService.getAllCourseOrderByCreatedTime();
        if (courses != null) {
            for (Course course : courses) {
                if(course.getPrice() != 0){
                    Account account = accountService.getAccountById(course.getAccountId());
                    CourseDTO courseDTO = new CourseDTO();
                    courseDTO.setCourse(course);
                    courseDTO.setTrainerName(account.getUsername());
                    courseDTO.setNumberOfRegister(0);
                    courseDTO.setNumberOfVideoInCourse(videoService.countVideosByCourseId(courseDTO.getCourse().getId()));
                    courseDTOS.add(courseDTO);
                }
            }
        }
        return courseDTOS;
    }
}
