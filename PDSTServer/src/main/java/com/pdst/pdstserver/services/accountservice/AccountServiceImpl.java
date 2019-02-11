package com.pdst.pdstserver.services.accountservice;

import com.pdst.pdstserver.models.Account;
import com.pdst.pdstserver.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public boolean create(Account account) {
        List<Account> checkExistedAccount = accountRepository.findByUsername(account.getUsername());
        if (checkExistedAccount.size() > 0) {
            return false;
        } else {
            accountRepository.save(account);
            return true;
        }
    }

    //Api for test
    @Override
    public List<Account> getAllAccounts(){
        return accountRepository.findAll();
    }

    @Override
    public void update(Account account) {
        accountRepository.save(account);
    }
}
