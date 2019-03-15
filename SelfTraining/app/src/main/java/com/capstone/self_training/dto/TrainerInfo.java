package com.capstone.self_training.dto;

import com.capstone.self_training.model.Account;

import java.io.Serializable;

public class TrainerInfo implements Serializable {
    Account account;
    private int totalRegister;
    private int totalCourse;

    public TrainerInfo() {
    }

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
}
