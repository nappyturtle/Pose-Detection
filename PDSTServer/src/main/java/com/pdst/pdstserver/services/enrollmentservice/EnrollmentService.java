package com.pdst.pdstserver.services.enrollmentservice;


import com.pdst.pdstserver.models.Enrollment;

import java.util.List;

public interface EnrollmentService {
    List<Enrollment> getAllEnrollments();
}
