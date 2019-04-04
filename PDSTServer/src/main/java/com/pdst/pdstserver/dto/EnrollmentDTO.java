package com.pdst.pdstserver.dto;

import java.io.Serializable;

public class EnrollmentDTO implements Serializable {
    private int id;
    private String status;
    private Integer courseId;
    private String  courseName;
    private Integer accountId;
    private String username;
    private Integer price;
    private String createdTime;
    private String updatedTime;
    private Integer totalVideo;
    private String thumbnail;
    private String accountThumbnail;

    public String getAccountThumbnail() {
        return accountThumbnail;
    }

    public void setAccountThumbnail(String accountThumbnail) {
        this.accountThumbnail = accountThumbnail;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Integer getTotalVideo() {
        return totalVideo;
    }

    public void setTotalVideo(Integer totalVideo) {
        this.totalVideo = totalVideo;
    }


}
