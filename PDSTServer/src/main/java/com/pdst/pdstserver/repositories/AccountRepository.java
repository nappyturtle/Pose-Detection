package com.pdst.pdstserver.repositories;

import com.pdst.pdstserver.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    Account findByUsername(String username);

    List<Account> findAllByRoleIdGreaterThanEqual(int role_id);

    Account findByUsernameAndStatusEquals(String username,String status);
    Account findAccountById(Integer id);

}
