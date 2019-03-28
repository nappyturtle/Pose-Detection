package com.pdst.pdstserver.controllers;

import com.pdst.pdstserver.constant.Constant;
import com.pdst.pdstserver.dtos.AccountDTO;
import com.pdst.pdstserver.dtos.TrainerInfo;
import com.pdst.pdstserver.models.Account;
import com.pdst.pdstserver.models.Course;
import com.pdst.pdstserver.security.SecurityConstants;
import com.pdst.pdstserver.services.accountservice.AccountService;
import com.pdst.pdstserver.services.courseservice.CourseService;
import com.pdst.pdstserver.services.enrollmentservice.EnrollmentService;
import com.pdst.pdstserver.services.videoservice.VideoService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

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

    public AccountController(AccountService accountService, CourseService courseService, EnrollmentService enrollmentService, VideoService videoService) {
        this.accountService = accountService;
        this.courseService = courseService;
        this.enrollmentService = enrollmentService;
        this.videoService = videoService;
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
    public ResponseEntity updateProfile(@RequestBody AccountDTO account) {
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

    @GetMapping("getDataForDashboard")
    public Map<String, Integer> getDataForDashboard() {
        int totalUser = 0, totalVideo = 0, totalCourse = 0;
        totalUser = accountService.countTotalUserAccount(3) + accountService.countTotalUserAccount(4);
        totalVideo = videoService.countAllVideos();
        totalCourse = courseService.countAllCourses();
        HashMap<String, Integer> map = new HashMap<>();
        map.put("totalUser", totalUser);
        map.put("totalVideo", totalVideo);
        map.put("totalCourse", totalCourse);
        return map;
    }

    @PostMapping("createNewAccount")
    public Map<String, String> createNewAccount(@RequestBody Account account) {
        HashMap<String, String> res = new HashMap<>();
        account.setImgUrl(Constant.DEFAULT_USER_LOGO_URL);
        boolean result = accountService.createAccount(account);
        if (result) {
            res.put("message", "success");
            Account newAccount = accountService.getAccountByUsername(account.getUsername().trim());
            if (newAccount != null && newAccount.getRoleId() == 3) {
                Course freeCourseOfNewTrainer = new Course();
                freeCourseOfNewTrainer.setAccountId(newAccount.getId());
                freeCourseOfNewTrainer.setStatus("active");
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
