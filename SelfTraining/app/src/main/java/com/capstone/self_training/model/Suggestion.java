package com.capstone.self_training.model;

import java.io.Serializable;

public class Suggestion implements Serializable{
    private int id;
    private String name;
    private int accountId;
    private int videoId;
    private String status;
    private String createdTime;
    private String updatedTime;
    private String imgUrl;
    public Suggestion(int id, String name, String createdTime, String imgUrl){
        this.id = id;
        this.name = name;
        this.createdTime = createdTime;
        this.imgUrl = imgUrl;
    }
    public Suggestion(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getVideoId() {
        return videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
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
}
