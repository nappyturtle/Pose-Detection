package com.pdst.pdstserver.dto;

import com.pdst.pdstserver.model.Account;

import java.io.Serializable;

public class TrainerInfo implements Serializable {
    Account account;
    private int totalRegister;
    private int totalCourse;

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public int getTotalRegister() {
        return totalRegister;
    }

    public void setTotalRegister(int totalRegister) {
        this.totalRegister = totalRegister;
    }

    public int getTotalCourse() {
        return totalCourse;
    }

    public void setTotalCourse(int totalCourse) {
        this.totalCourse = totalCourse;
    }

    public TrainerInfo() {
    }
}
