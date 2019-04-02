package com.pdst.pdstserver.enrollment;

import com.pdst.pdstserver.account.Account;
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
}
