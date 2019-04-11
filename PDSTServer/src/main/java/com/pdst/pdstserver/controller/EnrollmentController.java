package com.pdst.pdstserver.controller;

import com.pdst.pdstserver.dto.CourseDTOFrontEnd;
import com.pdst.pdstserver.dto.EnrollmentDTOFrontEnd;
import com.pdst.pdstserver.model.Account;
import com.pdst.pdstserver.dto.CourseDTO;
import com.pdst.pdstserver.model.Course;
import com.pdst.pdstserver.model.Enrollment;
import com.pdst.pdstserver.dto.EnrollmentDTO;
import com.pdst.pdstserver.service.AccountService;
import com.pdst.pdstserver.service.CourseService;
import com.pdst.pdstserver.service.EnrollmentService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(EnrollmentController.BASE_URL)
public class EnrollmentController {
    public static final String BASE_URL = "enrollment";
    private final EnrollmentService enrollmentService;
    private final CourseService courseService;
    private final AccountService accountService;

    public EnrollmentController(EnrollmentService enrollmentService, CourseService courseService, AccountService accountService) {
        this.enrollmentService = enrollmentService;
        this.courseService = courseService;
        this.accountService = accountService;
    }

    @GetMapping("enrollments")
    public List<Enrollment> getAllEnrollments() {
        return enrollmentService.getAllEnrollments();
    }

    @GetMapping("getAllBoughtCourse")
    public List<EnrollmentDTO> getAllBoughtCourseByAccountId(@RequestParam(value = "page") int page, @RequestParam(value = "size") int size, @RequestParam(value = "accountId") int accountId) {
        return enrollmentService.getAllEnrollmentByAccountId(page, size, accountId);
    }

    @GetMapping("getAllBoughtCourseTrainer")
    public List<EnrollmentDTO> getAllBoughtCourseWithTrainername(@RequestParam(value = "accountId") int accountId) {
        return enrollmentService.getAllBoughtCourseWithTrainername(accountId);
    }

    @GetMapping("getAllTrainerOfBoughtCourse")
    public List<Account> getAllTrainerOfBoughtCourse(@RequestParam(value = "accountId") int accountId) {
        return enrollmentService.getAllTrainerOfBoughtCourse(accountId);
    }

    @PostMapping("createEnrollment")
    public boolean createEnrollment(@RequestBody Enrollment enrollment) {
        return enrollmentService.saveToEnrollment(enrollment);
    }

    @GetMapping("checkBoughtCourseUpdatedByTrainer")
    public CourseDTO checkBoughtCourseUpdatedByTrainer(@RequestParam(value = "traineeId") int traineeId,
                                                       @RequestParam(value = "courseId") int courseId) {
        return enrollmentService.checkBoughtCourseUpdatedByTrainer(traineeId, courseId);
    }

    @GetMapping("checkEnrollmentExistedOrNot")
    public boolean checkEnrollmentExistedOrNot(@RequestParam(value = "traineeId") int traineeId,
                                               @RequestParam(value = "courseId") int courseId) {
        return enrollmentService.checkEnrollmentExistedOrNot(traineeId, courseId);
    }

    @GetMapping("enrollmentstats")
    public List<EnrollmentDTOFrontEnd> getEnrollmentForStats() {
        List<EnrollmentDTOFrontEnd> response = new ArrayList<>();
        List<Enrollment> enrollments = enrollmentService.getAllEnrollments();
        for (Enrollment enroll : enrollments) {
            EnrollmentDTOFrontEnd enrollmentDTOFrontEnd = new EnrollmentDTOFrontEnd();
            enrollmentDTOFrontEnd.setEnrollment(enroll);

            CourseDTOFrontEnd course = courseService.getCourseDetailById(enroll.getCourseId());
            enrollmentDTOFrontEnd.setCoursename(course.getCoursename());

            Account account = accountService.getAccountById(enroll.getAccountId());
            enrollmentDTOFrontEnd.setUsername(account.getUsername());

            response.add(enrollmentDTOFrontEnd);
        }
        return response;
    }

    @GetMapping("getBoughtCourseByAccountId/{accountId}")
    public List<Course> getBoughtCourseByAccountId(@PathVariable int accountId) {
        List<Enrollment> enrollments = enrollmentService.getEnrollmentByAccountId(accountId);
        List<Course> boughtCourseByAccountId = new ArrayList<>();
        if (enrollments != null) {
            for (Enrollment enroll : enrollments) {
                Course course = courseService.getCourseById(enroll.getCourseId());
                if (!boughtCourseByAccountId.contains(course)) {
                    boughtCourseByAccountId.add(course);
                }
            }
            return boughtCourseByAccountId;
        } else {
            return null;
        }
    }
}
