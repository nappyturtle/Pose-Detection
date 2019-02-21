package com.pdst.pdstserver.repositories;

import com.pdst.pdstserver.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    List<Account> findByUsername(String username);
    Account findAccountById(Integer id);
}
