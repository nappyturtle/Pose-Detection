package com.pdst.pdstserver.controllers;

import com.pdst.pdstserver.dtos.EnrollmentDTO;
import com.pdst.pdstserver.models.Enrollment;
import com.pdst.pdstserver.services.enrollmentservice.EnrollmentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
