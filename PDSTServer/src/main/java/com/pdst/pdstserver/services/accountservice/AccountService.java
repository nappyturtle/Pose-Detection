package com.pdst.pdstserver.services.accountservice;

import com.pdst.pdstserver.models.Account;

import java.util.List;

public interface AccountService {
    boolean create(Account account);
    List<Account> getAllAccounts();
    void update(Account account);
}
