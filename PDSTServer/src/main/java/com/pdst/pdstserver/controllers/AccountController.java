package com.pdst.pdstserver.controllers;

import com.pdst.pdstserver.services.accountservice.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(AccountController.BASE_URK)
public class AccountController {
    public static final String BASE_URK = "account";

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

}
