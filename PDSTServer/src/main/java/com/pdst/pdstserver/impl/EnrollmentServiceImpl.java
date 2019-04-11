package com.pdst.pdstserver.impl;


import com.pdst.pdstserver.dto.EnrollmentDTO;
import com.pdst.pdstserver.model.Account;
import com.pdst.pdstserver.model.Course;
import com.pdst.pdstserver.model.Enrollment;
import com.pdst.pdstserver.repository.AccountRepository;
import com.pdst.pdstserver.dto.CourseDTO;
import com.pdst.pdstserver.repository.CourseRepository;
import com.pdst.pdstserver.repository.EnrollmentRepository;
import com.pdst.pdstserver.model.Video;
import com.pdst.pdstserver.repository.VideoRepository;
import com.pdst.pdstserver.service.EnrollmentService;
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

    /**
     * @author KietPT
     * @since 6/4/2019
     * <p>
     * - hàm này dùng để lấy danh sách các course đã mua
     * - dùng cho mobile
     */
    @Override
    public List<EnrollmentDTO> getAllEnrollmentByAccountId(int page, int size, int accountId) {
        System.out.println("page - size = " + page + " - " + size);
        List<Enrollment> enrollmentList = enrollmentRepository.findAllByAccountId(PageRequest.of(page, size, Sort.by("createdTime").descending()), accountId);
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

    /**
     * @author KietPT
     * @since 6/4/2019
     * <p>
     * - hàm này dùng để lấy danh sách trainer từ những course đã mua
     * - dùng cho mobile
     */
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


    /**
     * @author KietPT
     * @since 6/4/2019
     * <p>
     * - hàm này dùng để tạo enrollment
     * - dùng cho mobile
     */
    @Override
    public boolean saveToEnrollment(Enrollment enrollment) {
        Enrollment resEnrollment = enrollmentRepository.save(enrollment);
        if (resEnrollment != null) {
            return true;
        }
        return false;
    }

    /**
     * @author KietPT
     * @since 6/4/2019
     * <p>
     * - hàm này dùng để lấy danh sách trainer từ những course đã mua
     * - dùng cho mobile
     */
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

    @Override
    public CourseDTO checkBoughtCourseUpdatedByTrainer(int traineeId, int courseId) {
        List<Enrollment> enrollmentList = enrollmentRepository.findAllByAccountId(traineeId);
        Course course = courseRepository.findCourseById(courseId);
        Account trainer = accountRepository.findAccountById(course.getAccountId());
        CourseDTO dto = new CourseDTO();
        int totalPriceEnrollment = 0;
        for (Enrollment enrollment : enrollmentList) {
            totalPriceEnrollment += enrollment.getPrice();
        }
        String maxDate = enrollmentRepository.getMaxDateEnrollmentByAccountIdAndCourseId(courseId, traineeId);
        System.out.println("maxDate = " + maxDate);

        if (totalPriceEnrollment < course.getPrice()) {

            List<Video> videoUpdated = videoRepository.findAllByCreatedTimeGreaterThanAndCourseId(maxDate, courseId);
            Course courseRes = new Course();
            courseRes.setId(course.getId());
            courseRes.setAccountId(course.getAccountId());
            courseRes.setThumbnail(course.getThumbnail());
            courseRes.setName(course.getName());
            courseRes.setPrice(course.getPrice() - totalPriceEnrollment);
            dto.setCourse(courseRes);
            dto.setTrainerName(trainer.getFullname());
            dto.setVideoUpdated(videoUpdated);
            return dto;
        }
        return null;
    }

    @Override
    public boolean checkEnrollmentExistedOrNot(int traineeId, int courseId) {
        List<Enrollment> enrollmentList = enrollmentRepository.findAllByAccountIdAndCourseId(traineeId,courseId);
        System.out.println(enrollmentList.size());
        if(enrollmentList == null || enrollmentList.size() == 0){
            return false;
        }

        return true;
    }

    @Override
    public List<Enrollment> getEnrollmentByAccountId(int accountId) {
        return enrollmentRepository.findAllByAccountId(accountId);
    }
}
