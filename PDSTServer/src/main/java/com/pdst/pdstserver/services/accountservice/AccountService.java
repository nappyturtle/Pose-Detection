package com.pdst.pdstserver.services.accountservice;

import com.pdst.pdstserver.dtos.AccountDTO;
import com.pdst.pdstserver.models.Account;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AccountService {
    boolean createAccount(Account account);

    List<Account> getAllAccounts();

    Account editProfile(AccountDTO account);

    List<Account> getAllAccountsByStaff();

    Account getAccountById(int id);

    Account loginForStaff(String username, String password);

    List<Account> getAllAccountByRoleId(int roleId);

    Account updateAccount(Account account);

    int countTotalUserAccount(int roleId);

    Account createNewAccount(Account account);

    Account getAccountByUsername(String username);
}
