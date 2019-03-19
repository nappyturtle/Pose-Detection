package com.pdst.pdstserver.services.enrollmentservice;


import com.pdst.pdstserver.dtos.EnrollmentDTO;
import com.pdst.pdstserver.models.Account;
import com.pdst.pdstserver.models.Course;
import com.pdst.pdstserver.models.Enrollment;
import com.pdst.pdstserver.repositories.AccountRepository;
import com.pdst.pdstserver.repositories.CourseRepository;
import com.pdst.pdstserver.repositories.EnrollmentRepository;
import com.pdst.pdstserver.repositories.VideoRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class EnrollmentServiceImpl implements EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final AccountRepository accountRepository;
    private final VideoRepository videoRepository;


    public EnrollmentServiceImpl(EnrollmentRepository enrollmentRepository, CourseRepository courseRepository,
                                 AccountRepository accountRepository, VideoRepository videoRepository
    ) {
        this.enrollmentRepository = enrollmentRepository;
        this.courseRepository = courseRepository;
        this.accountRepository = accountRepository;
        this.videoRepository = videoRepository;
    }

    @Override
    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }

    @Override
    public List<EnrollmentDTO> getAllEnrollmentByAccountId(int page, int size, int accountId) {
        System.out.println("page - size = " + page + " - " + size);
        List<Enrollment> enrollmentList = enrollmentRepository.findAllByAccountId(new PageRequest(page, size, Sort.Direction.DESC, "createdTime"), accountId);
        List<EnrollmentDTO> dtoList = new ArrayList<>();
        for (Enrollment enrollment : enrollmentList) {
            Course course = courseRepository.findCourseById(enrollment.getCourseId());
            Account account = accountRepository.findAccountById(course.getAccountId());
            Integer countVideo = videoRepository.countVideoByCourseId(course.getId());
            EnrollmentDTO dto = new EnrollmentDTO();
            dto.setId(enrollment.getId());
            dto.setCourseId(enrollment.getCourseId());
            dto.setCourseName(course.getName());
            dto.setAccountId(account.getId());
            dto.setUsername(account.getUsername());
            dto.setAccountThumbnail(account.getImgUrl());
            dto.setCreatedTime(enrollment.getCreatedTime());
            dto.setPrice(enrollment.getPrice());
            dto.setThumbnail(course.getThumbnail());
            dto.setTotalVideo(countVideo);
            dto.setStatus(course.getStatus());
            dtoList.add(dto);
        }
        return dtoList;
    }

    @Override
    public List<EnrollmentDTO> getAllBoughtCourseWithTrainername(int accountId) {
        List<Enrollment> enrollmentList = enrollmentRepository.findAllByAccountId(accountId);
        List<EnrollmentDTO> dtoList = new ArrayList<>();
        for (Enrollment enrollment : enrollmentList) {
            Course course = courseRepository.findCourseById(enrollment.getCourseId());
            Account account = accountRepository.findAccountById(course.getAccountId());
            EnrollmentDTO dto = new EnrollmentDTO();
            dto.setCourseId(course.getId());
            dto.setAccountId(account.getId());
            dto.setUsername(account.getUsername());
            dto.setAccountThumbnail(account.getImgUrl());
            dtoList.add(dto);
        }

        //remove duplicate items
        for (int i = 0; i < dtoList.size(); i++) {
            for (int j = i + 1; j < dtoList.size(); j++) {
                if (dtoList.get(i).getAccountId().equals(dtoList.get(j).getAccountId())) {
                    dtoList.remove(j);
                    j--;
                }
            }
        }
        return dtoList;
    }

    @Override
    public int countRegisterByCourseId(int courseID) {
        return enrollmentRepository.countAllByCourseId(courseID);
    }

    @Override
    public boolean saveToEnrollment(Enrollment enrollment) {
        Enrollment resEnrollment = enrollmentRepository.save(enrollment);
        if(resEnrollment != null){
            return true;
        }
        return false;
    }

    @Override
    public List<Account> getAllTrainerOfBoughtCourse(int accountId) {
        List<Enrollment> enrollmentList = enrollmentRepository.findAllByAccountId(accountId);
        List<Account> dtoList = new ArrayList<>();
        for (Enrollment enrollment : enrollmentList) {
            Course course = courseRepository.findCourseById(enrollment.getCourseId());
            Account account = accountRepository.findAccountById(course.getAccountId());

            dtoList.add(account);
        }

        //remove duplicate items
        for (int i = 0; i < dtoList.size(); i++) {
            for (int j = i + 1; j < dtoList.size(); j++) {
                if (dtoList.get(i).getId() == dtoList.get(j).getId()) {
                    dtoList.remove(j);
                    j--;
                }
            }
        }
        return dtoList;
    }
}
