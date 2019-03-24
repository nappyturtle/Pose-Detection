package com.pdst.pdstserver.services.courseservice;


import com.pdst.pdstserver.dtos.CourseDTO;
import com.pdst.pdstserver.dtos.CourseDTOFrontEnd;
import com.pdst.pdstserver.dtos.EnrollmentDTO;
import com.pdst.pdstserver.models.Account;
import com.pdst.pdstserver.models.Category;
import com.pdst.pdstserver.models.Course;
import com.pdst.pdstserver.models.Enrollment;
import com.pdst.pdstserver.repositories.*;
import com.pdst.pdstserver.services.enrollmentservice.EnrollmentService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@Service
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final AccountRepository accountRepository;
    private final VideoRepository videoRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final CategoryRepository categoryRepository;

    public CourseServiceImpl(CourseRepository courseRepository, AccountRepository accountRepository
            , VideoRepository videoRepository, EnrollmentRepository enrollmentRepository, CategoryRepository categoryRepository) {
        this.courseRepository = courseRepository;
        this.accountRepository = accountRepository;
        this.videoRepository = videoRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    public List<Course> getAllCourseByAccountId(int id) {
        return courseRepository.findAllByAccountIdOrderByCreatedTimeAsc(id);
    }

    @Override
    public List<CourseDTO> getAllCourseByTrainerId(int page, int size, int accountId) {
        System.out.println("page - size = " + page + " - " + size);
        List<Course> courseList = courseRepository.findAllByAccountIdOrderByCreatedTimeDesc(new PageRequest(page, size), accountId);
        List<CourseDTO> dtoList = new ArrayList<>();
        List<Account> traineeList = null;
        for (Course courseTemp : courseList) {
            Course course = courseRepository.findCourseById(courseTemp.getId());
            Account account = accountRepository.findAccountById(course.getAccountId());
            Integer countVideo = videoRepository.countVideoByCourseId(course.getId());
            List<Enrollment> enrollmentList = enrollmentRepository.findAllByCourseId(course.getId());
            traineeList = new ArrayList<>();
            for (Enrollment enrollment : enrollmentList) {
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
        if (res != null) {
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

    @Override
    public List<CourseDTOFrontEnd> getAllCourseByStaffOrAdmin() {
        List<Course> courses = courseRepository.findAll();
        List<CourseDTOFrontEnd> courseDTOFrontEnds = new ArrayList<>();
        for (Course c : courses) {
            CourseDTOFrontEnd courseDTOFrontEnd = new CourseDTOFrontEnd();
            Account account = accountRepository.findAccountById(c.getAccountId());
            Category category = categoryRepository.getOne(c.getCategoryId());
            courseDTOFrontEnd.setId(c.getId());
            courseDTOFrontEnd.setCoursename(c.getName());
            courseDTOFrontEnd.setCategoryname(category.getName());
            courseDTOFrontEnd.setAccountname(account.getUsername());
            courseDTOFrontEnd.setPrice(c.getPrice());
            courseDTOFrontEnd.setThumbnail(c.getThumbnail());
            courseDTOFrontEnd.setStatus(c.getStatus());
            courseDTOFrontEnds.add(courseDTOFrontEnd);
        }
        return courseDTOFrontEnds;
    }

    @Override
    public boolean editCourseByStaffOrAdmin(CourseDTOFrontEnd dto) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = Calendar.getInstance().getTime();
        Course course = courseRepository.findCourseById(dto.getId());
        course.setStatus(dto.getStatus());
        course.setUpdatedTime(sdf.format(date));
        Course courseRes = courseRepository.save(course);
        if (courseRes != null) {
            return true;
        }
        return false;
    }

    @Override
    public int countAllCourses() {
        return courseRepository.countAllCourses();
    }

}
