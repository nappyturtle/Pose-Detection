package com.pdst.pdstserver.services.enrollmentservice;


import com.pdst.pdstserver.dtos.EnrollmentDTO;
import com.pdst.pdstserver.models.Account;
import com.pdst.pdstserver.models.Course;
import com.pdst.pdstserver.models.Enrollment;

import java.util.List;

public interface EnrollmentService {
    List<Enrollment> getAllEnrollments();
    List<EnrollmentDTO> getAllEnrollmentByAccountId(int page, int size, int accountId);
    List<EnrollmentDTO> getAllBoughtCourseWithTrainername(int accountId);
    int countRegisterByCourseId(int courseID);
    boolean saveToEnrollment(Enrollment enrollment);
    List<Account> getAllTrainerOfBoughtCourse(int accountId);
}
