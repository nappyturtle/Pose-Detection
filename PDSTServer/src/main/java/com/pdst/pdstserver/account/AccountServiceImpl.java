package com.pdst.pdstserver.account;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Account> getAllAccountsByStaff() {
        return accountRepository.findAllByRoleIdGreaterThanEqual(3);
    }

    @Override
    public Account getAccountById(int id) {
        return accountRepository.findAccountById(id);
    }

    @Override
    public Account loginForStaff(String username, String password) {
        return accountRepository.findAccountByUsernameAndPassword(username, password);
    }

    @Override
    public List<Account> getAllAccountByRoleId(int roleId) {
        return accountRepository.findAllByRoleIdOrderByCreatedTimeDesc(roleId);
    }

    @Override
    public Account updateAccount(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public int countTotalUserAccount(int roleId) {
        return accountRepository.countAllByRoleId(roleId);
    }

    @Override
    public Account createNewAccount(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public Account getAccountByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

}
