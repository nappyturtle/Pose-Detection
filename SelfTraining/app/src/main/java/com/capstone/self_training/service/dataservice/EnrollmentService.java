package com.capstone.self_training.service.dataservice;

import com.capstone.self_training.dto.EnrollmentDTO;
import com.capstone.self_training.model.Account;
import com.capstone.self_training.model.Enrollment;
import com.capstone.self_training.service.iService.IEnrollmentService;

import java.util.List;

import retrofit2.Call;

public class EnrollmentService {
    private IEnrollmentService iEnrollmentService;

    public List<EnrollmentDTO> getBoughtCoursesByDate(String token, int page, int size, int accountId) {
        iEnrollmentService = DataService.getEnrollmentService();
        Call<List<EnrollmentDTO>> call = iEnrollmentService.getAllBoughtCourseByDate(token, page, size, accountId);
        List<EnrollmentDTO> enrollmentDTOS = null;
        try {
            enrollmentDTOS = call.execute().body();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return enrollmentDTOS;
    }

    public List<EnrollmentDTO> getAllBoughtCourseTrainername(String token, int accountId) {
        iEnrollmentService = DataService.getEnrollmentService();
        Call<List<EnrollmentDTO>> call = iEnrollmentService.getAllBoughtCourseTrainername(token, accountId);
        List<EnrollmentDTO> courseDTOS = null;
        try {
            courseDTOS = call.execute().body();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return courseDTOS;
    }

    public List<Account> getAllTrainerOfBoughtCourse(String token, int accountId) {
        iEnrollmentService = DataService.getEnrollmentService();
        Call<List<Account>> call = iEnrollmentService.getAllTrainerOfBoughtCourse(token, accountId);
        List<Account> accountList = null;
        try {
            accountList = call.execute().body();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return accountList;
    }

    public boolean createEnrollment(String token, Enrollment enrollment) {
        iEnrollmentService = DataService.getEnrollmentService();
        Call<Boolean> call = iEnrollmentService.createEnrollment(token, enrollment);
        try {
            return call.execute().body();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
