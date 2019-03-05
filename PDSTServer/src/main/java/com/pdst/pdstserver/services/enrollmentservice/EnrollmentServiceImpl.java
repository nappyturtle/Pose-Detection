package com.pdst.pdstserver.services.enrollmentservice;


import com.pdst.pdstserver.models.Enrollment;
import com.pdst.pdstserver.repositories.EnrollmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class EnrollmentServiceImpl implements EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;


    public EnrollmentServiceImpl(EnrollmentRepository enrollmentRepository) {
        this.enrollmentRepository = enrollmentRepository;
    }

    @Override
    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }
}
