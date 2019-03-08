package com.pdst.pdstserver.services.enrollmentservice;


import com.pdst.pdstserver.dtos.EnrollmentDTO;
import com.pdst.pdstserver.models.Enrollment;

import java.util.List;

public interface EnrollmentService {
    List<Enrollment> getAllEnrollments();
    List<EnrollmentDTO> getAllEnrollmentByAccountId(int page, int size, int accountId);
    List<EnrollmentDTO> getAllBoughtCourseWithTrainername(int accountId);
}
