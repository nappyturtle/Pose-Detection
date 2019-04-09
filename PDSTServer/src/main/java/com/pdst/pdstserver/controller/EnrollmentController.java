package com.pdst.pdstserver.controller;

import com.pdst.pdstserver.model.Account;
import com.pdst.pdstserver.dto.CourseDTO;
import com.pdst.pdstserver.model.Enrollment;
import com.pdst.pdstserver.dto.EnrollmentDTO;
import com.pdst.pdstserver.service.EnrollmentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(EnrollmentController.BASE_URL)
public class EnrollmentController {
    public static final String BASE_URL = "enrollment";
    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
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
}
