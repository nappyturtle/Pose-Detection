package com.pdst.pdstserver.controller;

import com.pdst.pdstserver.model.Account;
import com.pdst.pdstserver.service.*;
import com.pdst.pdstserver.dto.TrainerInfo;
import com.pdst.pdstserver.utils.Constant;
import com.pdst.pdstserver.model.Course;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(AccountController.BASE_URK)
public class AccountController {
    public static final String BASE_URK = "account";

    private final AccountService accountService;
    private final CourseService courseService;
    private final EnrollmentService enrollmentService;
    private final VideoService videoService;
    private final CategoryService categoryService;

    public AccountController(AccountService accountService, CourseService courseService, EnrollmentService enrollmentService, VideoService videoService, CategoryService categoryService) {
        this.accountService = accountService;
        this.courseService = courseService;
        this.enrollmentService = enrollmentService;
        this.videoService = videoService;
        this.categoryService = categoryService;
    }


    @GetMapping("accounts")
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @PostMapping("sign-up")
    public ResponseEntity createAccount(@RequestBody Account account) {
        boolean flag = accountService.createAccount(account);
        if (flag == false) {
            return ResponseEntity.status(CONFLICT).body("Username của đã tồn tại, vui lòng chon username khác");
        }
        return ResponseEntity.status(CREATED).body("Đăng kí thành công");

    }

    @GetMapping("staff/accounts")
    public List<Account> findAllAccountsByStaff() {
        return accountService.getAllAccountsByStaff();
    }

    @PutMapping("edit")
    public ResponseEntity updateProfile(@RequestBody Account account) {
        Account accountEdited = accountService.editProfile(account);
        if (accountEdited != null) {
            return ResponseEntity.status(OK).body(accountEdited);
        }
        return ResponseEntity.status(NOT_FOUND).body("Tài khoản này không tồn tại");
    }

    @GetMapping("update/{id}")
    public Account getAccount(@PathVariable(value = "id") int id) {
        return accountService.getAccountById(id);
    }

    @GetMapping("getTrainerInfo")
    public TrainerInfo getTrainerInfo(@RequestParam(value = "accountId") int accountId) {
        List<Course> courseList = new ArrayList<>();
        int totalRegister = 0;
        Account trainerAccount = accountService.getAccountById(accountId);
        int totalTrainerCourse = courseService.countAllCoursesByAccountId(accountId);
        courseList = courseService.getAllCoursesWithPriceByAccountId(accountId);
        if (courseList != null) {
            for (Course c : courseList) {
                int courseId = c.getId();
                int registerInCourse = enrollmentService.countRegisterByCourseId(courseId);
                totalRegister += registerInCourse;
            }
        }
        TrainerInfo trainerInfo = new TrainerInfo();
        trainerInfo.setAccount(trainerAccount);
        trainerInfo.setTotalCourse(totalTrainerCourse);
        trainerInfo.setTotalRegister(totalRegister);
        return trainerInfo;
    }

    @PostMapping("updateAccount")
    public boolean updateAccount(@RequestBody Account account) {
        Account accountUpdated = accountService.updateAccount(account);
        if (accountUpdated == null) {
            return false;
        } else {
            return true;
        }
    }

    @GetMapping("getAllAccountByRoleId")
    public List<Account> getAllAccountByRoleId(@RequestParam(value = "roleId") int roleId) {
        return accountService.getAllAccountByRoleId(roleId);
    }

    @GetMapping("getDataForDashboard/{roleId}")
    public Map<String, Integer> getDataForDashboard(@PathVariable int roleId) {
        int totalUser = 0, totalVideo = 0, totalCourse = 0, totalCate = 0;

        HashMap<String, Integer> map = new HashMap<>();
        if (roleId == 1) {
            totalUser = accountService.countTotalUserAccount(2);
            map.put("totalUser", totalUser);
            return map;
        } else if (roleId == 2) {
            totalUser = accountService.countTotalUserAccount(3) + accountService.countTotalUserAccount(4);
            totalVideo = videoService.countAllVideos();
            totalCourse = courseService.countAllCourses();
            totalCate = categoryService.countTotalCategories();
            map.put("totalUser", totalUser);
            map.put("totalVideo", totalVideo);
            map.put("totalCourse", totalCourse);
            map.put("totalCate", totalCate);
            return map;
        }
        return null;
    }

    @PostMapping("createNewAccount")
    public Map<String, String> createNewAccount(@RequestBody Account account) {
        HashMap<String, String> res = new HashMap<>();
        account.setImgUrl(Constant.DEFAULT_USER_LOGO_URL);
        account.setPassword("123456");
        boolean result = accountService.createAccount(account);
        if (result) {
            res.put("message", "success");
            Account newAccount = accountService.getAccountByUsername(account.getUsername().trim());
            if (newAccount != null && newAccount.getRoleId() == 3) {
                Course freeCourseOfNewTrainer = new Course();
                freeCourseOfNewTrainer.setAccountId(newAccount.getId());
                freeCourseOfNewTrainer.setStatus("active");
                freeCourseOfNewTrainer.setPrevStatus("active");
                freeCourseOfNewTrainer.setName("Miễn phí");
                freeCourseOfNewTrainer.setCreatedTime(newAccount.getCreatedTime());
                freeCourseOfNewTrainer.setPrice(0);
                freeCourseOfNewTrainer.setThumbnail(Constant.DEFAULT_APP_LOGO_URL);
                courseService.createCourse(freeCourseOfNewTrainer);
            }
        } else {
            res.put("message", "fail");
        }

        return res;
    }
}
