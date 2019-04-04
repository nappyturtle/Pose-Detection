package com.pdst.pdstserver.dto;


import java.io.Serializable;

public class SuggestionDTO implements Serializable {
    private int id;
    private String videoName;
    private Integer accountId;
    private Integer videoId;
    private String createdTime;
    private String thumnailUrl;
    private String urlVideoTrainee;
    private String foldernameTrainee;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFoldernameTrainee() {
        return foldernameTrainee;
    }

    public void setFoldernameTrainee(String foldernameTrainee) {
        this.foldernameTrainee = foldernameTrainee;
    }

    public String getUrlVideoTrainee() {
        return urlVideoTrainee;
    }

    public void setUrlVideoTrainee(String urlVideoTrainee) {
        this.urlVideoTrainee = urlVideoTrainee;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getVideoId() {
        return videoId;
    }

    public void setVideoId(Integer videoId) {
        this.videoId = videoId;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getThumnailUrl() {
        return thumnailUrl;
    }

    public void setThumnailUrl(String thumnailUrl) {
        this.thumnailUrl = thumnailUrl;
    }


}
