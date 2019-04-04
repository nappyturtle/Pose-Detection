package com.pdst.pdstserver.enrollment;


import com.pdst.pdstserver.account.Account;
import com.pdst.pdstserver.course.CourseDTO;

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
}
