package com.pdst.pdstserver.service;


import com.pdst.pdstserver.dto.EnrollmentDTO;
import com.pdst.pdstserver.model.Account;
import com.pdst.pdstserver.dto.CourseDTO;
import com.pdst.pdstserver.model.Enrollment;

import java.util.List;

public interface EnrollmentService {
    List<Enrollment> getAllEnrollments();
    List<EnrollmentDTO> getAllEnrollmentByAccountId(int page, int size, int accountId);
    List<EnrollmentDTO> getAllBoughtCourseWithTrainername(int accountId);
    int countRegisterByCourseId(int courseID);
    boolean saveToEnrollment(Enrollment enrollment);
    List<Account> getAllTrainerOfBoughtCourse(int accountId);
    CourseDTO checkBoughtCourseUpdatedByTrainer(int traineeId, int courseId);
    boolean checkEnrollmentExistedOrNot(int traineeId, int courseId);
    List<Enrollment> getEnrollmentByAccountId(int accountId);
}
