package com.capstone.self_training.model;

import java.io.Serializable;

public class UpgradeCourseTransaction implements Serializable {
    private int id;
    private String createdTime;
    private Integer courseId;
    private Integer price;
    private Integer videoLimit;
    private Integer enrollmentLimit;

    public UpgradeCourseTransaction() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getVideoLimit() {
        return videoLimit;
    }

    public void setVideoLimit(Integer videoLimit) {
        this.videoLimit = videoLimit;
    }

    public Integer getEnrollmentLimit() {
        return enrollmentLimit;
    }

    public void setEnrollmentLimit(Integer enrollmentLimit) {
        this.enrollmentLimit = enrollmentLimit;
    }
}
