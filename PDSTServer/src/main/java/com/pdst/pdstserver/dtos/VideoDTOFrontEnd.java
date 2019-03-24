package com.pdst.pdstserver.dtos;

import java.io.Serializable;

public class VideoDTOFrontEnd implements Serializable {
    private int id;
    private String title;
    private String thumnailUrl;
    private String contentUrl;
    private Integer numOfView;
    private String courseName;
    private String status;

    public VideoDTOFrontEnd() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumnailUrl() {
        return thumnailUrl;
    }

    public void setThumnailUrl(String thumnailUrl) {
        this.thumnailUrl = thumnailUrl;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public Integer getNumOfView() {
        return numOfView;
    }

    public void setNumOfView(Integer numOfView) {
        this.numOfView = numOfView;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}
