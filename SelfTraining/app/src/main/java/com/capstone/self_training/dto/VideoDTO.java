package com.capstone.self_training.dto;

import com.capstone.self_training.model.Video;

import java.io.Serializable;

public class VideoDTO implements Serializable {
    private Video video;
    private String username;
    private String imgUrl;

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

    public VideoDTO() {
    }
}
