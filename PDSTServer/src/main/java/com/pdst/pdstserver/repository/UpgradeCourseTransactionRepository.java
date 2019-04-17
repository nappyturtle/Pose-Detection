package com.pdst.pdstserver.repository;


import com.pdst.pdstserver.model.UpgradeCourseTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UpgradeCourseTransactionRepository extends JpaRepository<UpgradeCourseTransaction, Integer> {
    List<UpgradeCourseTransaction> findAllByCourseIdEquals(int courseId);
}
