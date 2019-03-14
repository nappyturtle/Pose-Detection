package com.capstone.self_training.model;

import java.io.Serializable;

public class Enrollment implements Serializable {
    private int id;
    private Integer courseId;
    private Integer accountId;
    private String createdTime;
    private Integer price;

    public Enrollment(Integer courseId, Integer accountId, Integer price,String createdTime) {
        this.courseId = courseId;
        this.accountId = accountId;
        this.price = price;
        this.createdTime = createdTime;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

}
