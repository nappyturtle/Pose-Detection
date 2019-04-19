package com.pdst.pdstserver.impl;

import com.pdst.pdstserver.model.UpgradeCourseTransaction;
import com.pdst.pdstserver.repository.UpgradeCourseTransactionRepository;
import com.pdst.pdstserver.service.UpgradeCourseTransactionService;
import org.springframework.stereotype.Service;

@Service
public class UpgradeCourseTransactionServiceImpl implements UpgradeCourseTransactionService {
    private UpgradeCourseTransactionRepository upgradeCourseTransactionRepository;

    public UpgradeCourseTransactionServiceImpl (UpgradeCourseTransactionRepository upgradeCourseTransactionRepository){
        this.upgradeCourseTransactionRepository = upgradeCourseTransactionRepository;
    }

    @Override
    public UpgradeCourseTransaction createTransactionForCreateNewCourseFee(UpgradeCourseTransaction transaction) {
        return upgradeCourseTransactionRepository.save(transaction);
    }
}
