package com.pdst.pdstserver.controller;

import com.pdst.pdstserver.model.Course;
import com.pdst.pdstserver.dto.CourseDTO;
import com.pdst.pdstserver.dto.CourseDTOFrontEnd;
import com.pdst.pdstserver.model.UpgradeCourseTransaction;
import com.pdst.pdstserver.service.*;
import com.pdst.pdstserver.dto.EnrollmentDTO;
import com.pdst.pdstserver.utils.Constant;
import com.pdst.pdstserver.utils.SearchUtil;
import com.pdst.pdstserver.model.Account;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(CourseController.BASE_URL)
public class CourseController {
    public static final String BASE_URL = "course";
    private final CourseService courseService;
    private final AccountService accountService;
    private final VideoService videoService;
    private final EnrollmentService enrollmentService;
    private final UpgradeCourseTransactionService upgradeCourseTransactionService;

    public CourseController(CourseService courseService, AccountService accountService,
                            VideoService videoService, EnrollmentService enrollmentService,
                            UpgradeCourseTransactionService upgradeCourseTransactionService) {
        this.courseService = courseService;
        this.accountService = accountService;
        this.videoService = videoService;
        this.enrollmentService = enrollmentService;
        this.upgradeCourseTransactionService = upgradeCourseTransactionService;
    }

    @GetMapping("courses")
    public List<Course> getAllEnrollments() {
        return courseService.getAllCourses();
    }

    @GetMapping("getCourseDetailById/{courseId}")
    public CourseDTOFrontEnd getCourseDetailById(@PathVariable(value = "courseId") int courseId) {
        return courseService.getCourseDetailById(courseId);
    }


    @GetMapping("getAllCourseByStaffOrAdmin")
    public List<CourseDTOFrontEnd> getAllCourseByStaffOrAdmin() {
        return courseService.getAllCourseByStaffOrAdmin();
    }

    @PutMapping("editCourseByStaffOrAdmin")
    public boolean editCourseByStaffOrAdmin(@RequestBody CourseDTOFrontEnd dto) {
        System.out.println(dto.getId() + " - " + dto.getStatus());
        return courseService.editCourseByStaffOrAdmin(dto);
    }


    @GetMapping("getAllCoursesByAccountId")
    public List<Course> getAllCoursesByAccountId(@RequestParam(value = "accountId") int id) {
        return courseService.getAllCourseByAccountId(id);
    }

    @GetMapping("getAllCourseByTrainerId")
    public List<CourseDTO> getAllCourseByTrainerId(@RequestParam(value = "page") int page,
                                                   @RequestParam(value = "size") int size,
                                                   @RequestParam(value = "accountId") int id) {
        return courseService.getAllCourseByTrainerId(page, size, id);
    }

    @PostMapping("create")
    public ResponseEntity<Void> createCourse(@RequestBody CourseDTO course) {
        Course result = courseService.createCourse(course.getCourse());
        int courseId = result.getId();
        if (result != null) {
            UpgradeCourseTransaction newTransaction = new UpgradeCourseTransaction();
            newTransaction.setCourseId(courseId);
            newTransaction.setPrice(course.getCreateCourseFee());
            newTransaction.setCreatedTime(result.getCreatedTime());
            newTransaction.setEnrollmentLimit(result.getEnrollmentLimit());
            newTransaction.setVideoLimit(result.getVideoLimit());
            UpgradeCourseTransaction transaction = upgradeCourseTransactionService.createTransactionForCreateNewCourseFee(newTransaction);
            if (transaction == null) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            return new ResponseEntity<Void>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    //get all courses which have price
    //number of regis not yet complete
    @GetMapping("getAllCourses")
    public List<CourseDTO> getAllCoursesWithPrice() {
        List<Course> courses = new ArrayList<>();
        List<CourseDTO> courseDTOS = new ArrayList<>();
        courses = courseService.getAllCourseOrderByCreatedTime();
        if (courses != null) {
            for (Course course : courses) {
                if (course.getPrice() != 0) {
                    Account account = accountService.getAccountById(course.getAccountId());
                    CourseDTO courseDTO = new CourseDTO();
                    courseDTO.setCourse(course);
                    courseDTO.setTrainerName(account.getUsername());
                    courseDTO.setNumberOfRegister(enrollmentService.countRegisterByCourseId(courseDTO.getCourse().getId()));
                    courseDTO.setNumberOfVideoInCourse(videoService.countVideosByCourseId(courseDTO.getCourse().getId()));
                    courseDTOS.add(courseDTO);
                }
            }
        }
        return courseDTOS;
    }

    @GetMapping("getAllCoursesWithPriceByAccountId")
    public List<CourseDTO> getAllCoursesWithPriceByAccountId(@RequestParam(value = "accountId") int accountId) {
        List<Course> courses = new ArrayList<>();
        List<CourseDTO> courseDTOS = new ArrayList<>();
        courses = courseService.getAllCoursesWithPriceByAccountId(accountId);
        if (courses != null) {
            for (Course course : courses) {
                if (course.getPrice() != 0) {
                    if (videoService.countVideosByCourseId(course.getId()) > 0) {
                        Account account = accountService.getAccountById(course.getAccountId());
                        CourseDTO courseDTO = new CourseDTO();
                        courseDTO.setCourse(course);
                        courseDTO.setTrainerName(account.getUsername());
                        courseDTO.setNumberOfRegister(enrollmentService.countRegisterByCourseId(courseDTO.getCourse().getId()));
                        courseDTO.setNumberOfVideoInCourse(videoService.countVideosByCourseId(courseDTO.getCourse().getId()));
                        courseDTOS.add(courseDTO);
                    }
                }
            }
        }
        return courseDTOS;
    }

    //neu accountId la 0 -> getAllCourses -> sẽ comment hàm getAllCoursesWithPrice
    @GetMapping("getUnboughtCourses")
    public List<CourseDTO> getUnboughtCourses(@RequestParam(value = "accountId") int accountId) {
        List<Course> courses = new ArrayList<>();
        List<CourseDTO> courseDTOS = new ArrayList<>();
        courses = courseService.getAllCourseOrderByCreatedTime();
        List<EnrollmentDTO> enrollments = enrollmentService.getAllBoughtCourseWithTrainername(accountId);
        boolean isBought;
        if (courses != null) {
            for (Course course : courses) {
                isBought = false;
                for (EnrollmentDTO enrollmentDTO : enrollments) {
                    if (course.getId() == enrollmentDTO.getCourseId()) {
                        isBought = true;
                        break;
                    }
                }
                if (course.getPrice() != 0 && !isBought && course.getAccountId() != accountId) {
                    if (videoService.countVideosByCourseId(course.getId()) > 0) {
                        Account account = accountService.getAccountById(course.getAccountId());
                        CourseDTO courseDTO = new CourseDTO();
                        courseDTO.setCourse(course);
                        courseDTO.setTrainerName(account.getUsername());
                        courseDTO.setNumberOfRegister(enrollmentService.countRegisterByCourseId(courseDTO.getCourse().getId()));
                        courseDTO.setNumberOfVideoInCourse(videoService.countVideosByCourseId(courseDTO.getCourse().getId()));
                        courseDTOS.add(courseDTO);
                    }
                }
            }
        }
        return courseDTOS;
    }

    @GetMapping("search")
    public List<CourseDTO> searchCourse(@RequestParam(value = "searchValue") String searchValue, @RequestParam(value = "accountId") int accountId) {

        List<CourseDTO> courseDTOS = new ArrayList<>();
        Map<Course, Double> courseMap = new HashMap();
        String[] tokens = searchValue.toLowerCase().split(" ");
        List<Course> courses = courseService.getAllCourseOrderByCreatedTime();
        List<EnrollmentDTO> enrollments = enrollmentService.getAllBoughtCourseWithTrainername(accountId);
        boolean isBought;
        if (courses != null) {
            for (Course course : courses) {
                isBought = false;
                for (EnrollmentDTO enrollmentDTO : enrollments) {
                    if (course.getId() == enrollmentDTO.getCourseId()) {
                        isBought = true;
                        break;
                    }
                }
                if (course.getPrice() != 0 && !isBought) {
                    courseMap.put(course, SearchUtil.searchMatchingPercentage(tokens, course.getName().toLowerCase()));
                }
            }
            courseMap = courseMap.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                            (e1, e2) -> e1, LinkedHashMap::new));
            Iterator iterator = courseMap.keySet().iterator();
            while (iterator.hasNext()) {
                Course course = (Course) iterator.next();
                if (courseMap.get(course) >= 0.5) {
//                    System.out.println("%: " + courseMap.get(course));
                    Account account = accountService.getAccountById(course.getAccountId());
                    CourseDTO courseDTO = new CourseDTO();
                    courseDTO.setCourse(course);
                    courseDTO.setTrainerName(account.getUsername());
                    courseDTO.setNumberOfRegister(enrollmentService.countRegisterByCourseId(courseDTO.getCourse().getId()));
                    courseDTO.setNumberOfVideoInCourse(videoService.countVideosByCourseId(courseDTO.getCourse().getId()));
                    courseDTOS.add(courseDTO);
                }
            }
        }
        return courseDTOS;
    }

    @PutMapping(value = "edit")
    public boolean editCourse(@RequestBody Course course) {
        Course courseBeForeUpdate = courseService.getCourseById(course.getId());
        int videoLimitExtra = Math.abs(course.getVideoLimit() - courseBeForeUpdate.getVideoLimit());
        int enrollmentLimitExtra = Math.abs(course.getEnrollmentLimit() - courseBeForeUpdate.getEnrollmentLimit());
        if (videoLimitExtra > 0 || enrollmentLimitExtra > 0) {
            UpgradeCourseTransaction updateTransaction = new UpgradeCourseTransaction();
            updateTransaction.setCourseId(course.getId());
            updateTransaction.setVideoLimit(videoLimitExtra);
            updateTransaction.setEnrollmentLimit(enrollmentLimitExtra);
            int courseFeeBeforeUpdate = courseBeForeUpdate.getVideoLimit() * courseBeForeUpdate.getEnrollmentLimit() * Constant.FREE_SUGGESTION_TURN_FOR_TRAINEE * Constant.PRICE_OF_A_SUGGESTION_TURN;
            int courseFeeAfterUpdate = course.getVideoLimit() * course.getEnrollmentLimit() * Constant.PRICE_OF_A_SUGGESTION_TURN * Constant.FREE_SUGGESTION_TURN_FOR_TRAINEE;
            int courseFeeExtra = Math.abs(courseFeeAfterUpdate - courseFeeBeforeUpdate);
            updateTransaction.setPrice(courseFeeExtra);
            updateTransaction.setCreatedTime(course.getUpdatedTime());
            UpgradeCourseTransaction transaction = upgradeCourseTransactionService.createTransactionForCreateNewCourseFee(updateTransaction);
        }
        return courseService.editCourse(course);
    }
}
