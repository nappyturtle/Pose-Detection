package com.capstone.self_training.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Suggestion {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("videoName")
    @Expose
    private String videoName;
    @SerializedName("accountId")
    @Expose
    private Integer accountId;
    @SerializedName("videoId")
    @Expose
    private Integer videoId;
    @SerializedName("createdTime")
    @Expose
    private Object createdTime;
    @SerializedName("thumnailUrl")
    @Expose
    private String thumnailUrl;
    @SerializedName("urlVideoTrainee")
    @Expose
    private String urlVideoTrainee;
    @SerializedName("foldernameTrainee")
    @Expose
    private String foldernameTrainee;
    @SerializedName("status")
    @Expose
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Object getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Object createdTime) {
        this.createdTime = createdTime;
    }

    public String getThumnailUrl() {
        return thumnailUrl;
    }

    public void setThumnailUrl(String thumnailUrl) {
        this.thumnailUrl = thumnailUrl;
    }

}