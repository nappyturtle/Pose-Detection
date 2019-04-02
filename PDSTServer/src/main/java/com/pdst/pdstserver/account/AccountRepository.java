package com.pdst.pdstserver.account;

import com.pdst.pdstserver.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    Account findByUsername(String username);

    List<Account> findAllByRoleIdGreaterThanEqual(int role_id);

    Account findByUsernameAndStatusEquals(String username, String status);

    Account findAccountById(Integer id);

    Account findAccountByUsernameAndPassword(String username, String password);

    List<Account> findAllByRoleIdOrderByCreatedTimeDesc(int roleId);

    int countAllByRoleId(int roleId);

}
