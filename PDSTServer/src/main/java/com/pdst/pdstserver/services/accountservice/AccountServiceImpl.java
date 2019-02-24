package com.pdst.pdstserver.services.accountservice;

import com.pdst.pdstserver.dtos.AccountDTO;
import com.pdst.pdstserver.models.Account;
import com.pdst.pdstserver.repositories.AccountRepository;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AccountServiceImpl(AccountRepository accountRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.accountRepository = accountRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public boolean createAccount(Account account) {
        System.out.println(account.getUsername());
        Account checkExistedAccount = accountRepository.findByUsername(account.getUsername());
        if (checkExistedAccount != null) {
            return false;
        }
        account.setCreatedTime(LocalDateTime.now().toString());
        account.setPassword(bCryptPasswordEncoder.encode(account.getPassword()));
        accountRepository.save(account);
        return true;
    }

    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public Account editProfile(AccountDTO account) {
        try {
            Account checkExistedAccount = accountRepository.findAccountById(account.getId());
            if (checkExistedAccount != null) {
                System.out.println("da vao aaaaaaaaaaaaaaaaaaaaaaaaa");
                    checkExistedAccount.setEmail(account.getEmail());
                    checkExistedAccount.setPhone(account.getPhone());
                    checkExistedAccount.setGender(account.getGender());
                    checkExistedAccount.setImgUrl(account.getImgUrl());
                    checkExistedAccount.setAddress(account.getAddress());
                    checkExistedAccount.setRoleId(account.getRoleId());
                    checkExistedAccount.setStatus(account.getStatus());
                    checkExistedAccount.setUpdatedTime(LocalDateTime.now().toString());

                return accountRepository.save(checkExistedAccount);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Account> getAllAccountsByStaff() {
        return accountRepository.findAllByRoleIdGreaterThanEqual(3);
    }


}
