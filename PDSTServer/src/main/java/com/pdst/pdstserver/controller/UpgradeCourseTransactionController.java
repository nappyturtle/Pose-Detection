package com.pdst.pdstserver.controller;

import com.pdst.pdstserver.service.UpgradeCourseTransactionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(UpgradeCourseTransactionController.BASE_URL)
public class UpgradeCourseTransactionController {
    public static final String BASE_URL = "upgradecourse";
    private final UpgradeCourseTransactionService upgradeCourseTransactionService;

    public UpgradeCourseTransactionController(UpgradeCourseTransactionService upgradeCourseTransactionService) {
        this.upgradeCourseTransactionService = upgradeCourseTransactionService;
    }
}
