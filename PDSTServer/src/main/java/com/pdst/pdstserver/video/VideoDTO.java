package com.pdst.pdstserver.video;

import com.pdst.pdstserver.video.Video;

import java.io.Serializable;

public class VideoDTO implements Serializable {
    private Video video;
    private String username;
    private String imgUrl;
    private int accountId;


    public VideoDTO() {
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
