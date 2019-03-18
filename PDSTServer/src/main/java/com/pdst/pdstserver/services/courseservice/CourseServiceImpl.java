package com.pdst.pdstserver.services.courseservice;


import com.pdst.pdstserver.dtos.CourseDTO;
import com.pdst.pdstserver.dtos.EnrollmentDTO;
import com.pdst.pdstserver.models.Account;
import com.pdst.pdstserver.models.Course;
import com.pdst.pdstserver.models.Enrollment;
import com.pdst.pdstserver.repositories.AccountRepository;
import com.pdst.pdstserver.repositories.CourseRepository;
import com.pdst.pdstserver.repositories.EnrollmentRepository;
import com.pdst.pdstserver.repositories.VideoRepository;
import com.pdst.pdstserver.services.enrollmentservice.EnrollmentService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final AccountRepository accountRepository;
    private final VideoRepository videoRepository;
    private final EnrollmentRepository enrollmentRepository;

    public CourseServiceImpl(CourseRepository courseRepository, AccountRepository accountRepository
    ,VideoRepository videoRepository ,EnrollmentRepository enrollmentRepository) {
        this.courseRepository = courseRepository;
        this.accountRepository = accountRepository;
        this.videoRepository = videoRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    public List<Course> getAllCourseByAccountId(int id) {
        return courseRepository.findAllByAccountId(id);
    }

    @Override
    public List<CourseDTO> getAllCourseByTrainerId(int page, int size, int accountId) {
        System.out.println("page - size = " + page + " - " + size);
        List<Course> courseList = courseRepository.findAllByAccountId(new PageRequest(page, size, Sort.Direction.DESC, "createdTime"), accountId);
        List<CourseDTO> dtoList = new ArrayList<>();
        List<Account> traineeList = null;
        for (Course courseTemp : courseList) {
            Course course = courseRepository.findCourseById(courseTemp.getId());
            Account account = accountRepository.findAccountById(course.getAccountId());
            Integer countVideo = videoRepository.countVideoByCourseId(course.getId());
            List<Enrollment> enrollmentList = enrollmentRepository.findAllByCourseId(course.getId());
            traineeList = new ArrayList<>();
            for(Enrollment enrollment : enrollmentList){
                Account trainee = accountRepository.findAccountById(enrollment.getAccountId());
                traineeList.add(trainee);
            }
            CourseDTO dto = new CourseDTO();
            dto.setCourse(course);
            dto.setTrainerName(account.getUsername());
            dto.setNumberOfVideoInCourse(countVideo);
            dto.setTraineeList(traineeList);
            dtoList.add(dto);
        }
        return dtoList;
    }

    @Override
    public boolean createCourse(Course course) {
        Course resCourse = courseRepository.save(course);
        if (resCourse != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean editCourse(Course course) {
        Course temp = courseRepository.findCourseById(course.getId());
        temp.setId(course.getId());
        temp.setUpdatedTime(course.getUpdatedTime());
        temp.setName(course.getName());
        temp.setCategoryId(course.getCategoryId());
        temp.setThumbnail(course.getThumbnail());
        temp.setPrice(course.getPrice());
        Course res = courseRepository.save(temp);
        if(res != null){
            return true;
        }
        return false;
    }

    @Override
    public List<Course> getAllCourseOrderByCreatedTime() {
        return courseRepository.findAllByOrderByCreatedTimeDesc();
    }

    @Override
    public List<Course> getAllCoursesWithPriceByAccountId(int accountId) {
        return courseRepository.getAllCoursesWithPriceByAccountId(accountId);
    }

    @Override
    public int countAllCoursesByAccountId(int accountId) {
        return courseRepository.countAllCoursesByAccountId(accountId);
    }

}
