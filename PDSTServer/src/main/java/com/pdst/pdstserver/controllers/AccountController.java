package com.pdst.pdstserver.controllers;

import com.pdst.pdstserver.models.Account;
import com.pdst.pdstserver.services.accountservice.AccountService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping(AccountController.BASE_URK)
public class AccountController {
    public static final String BASE_URK = "account";

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("accounts")
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @PostMapping("signin")
    public boolean checkLogin(@RequestBody String username, @RequestBody String password) {
        boolean loginFlag = accountService.checkLogin(username, password);
        if (loginFlag) {
            return true;
        } else {
            return false;
        }
    }

    @PostMapping("signup")
    public ResponseEntity<Void> addArticle(@RequestBody Account account, UriComponentsBuilder builder) {
        boolean flag = accountService.create(account);
        if (flag == false) {
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.path("create/{username}").buildAndExpand(account.getUsername()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    @PostMapping("register")
    public boolean register(@RequestBody Account account){
        boolean result = accountService.create(account);
        return result;
    }

    /*@PutMapping("update")
    public ResponseEntity<Account> updateArticle(@RequestBody Account account) {
        accountService.update(account);
        return new ResponseEntity<Account>(account, HttpStatus.OK);
    }*/

}
