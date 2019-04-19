package com.pdst.pdstserver.service;


import com.pdst.pdstserver.model.UpgradeCourseTransaction;

public interface UpgradeCourseTransactionService {
    UpgradeCourseTransaction createTransactionForCreateNewCourseFee(UpgradeCourseTransaction transaction);
}
