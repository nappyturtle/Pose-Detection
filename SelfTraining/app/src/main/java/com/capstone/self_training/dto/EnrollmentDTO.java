package com.capstone.self_training.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class EnrollmentDTO implements Serializable {
//    @SerializedName("id")
//    @Expose
    private int id;
//    @SerializedName("status")
//    @Expose
    private String status;
//    @SerializedName("courseId")
//    @Expose
    private Integer courseId;
//    @SerializedName("courseName")
//    @Expose
    private String  courseName;
//    @SerializedName("accountId")
//    @Expose
    private Integer accountId;
//    @SerializedName("username")
//    @Expose
    private String username;
//    @SerializedName("price")
//    @Expose
    private Integer price;
//    @SerializedName("createdTime")
//    @Expose
    private String createdTime;
//    @SerializedName("updatedTime")
//    @Expose
    private String updatedTime;
//    @SerializedName("totalVideo")
//    @Expose
    private Integer totalVideo;
//    @SerializedName("thumbnail")
//    @Expose
    private String thumbnail;
//    @SerializedName("accountThumbnail")
//    @Expose
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
