package com.pdst.pdstserver.dto;

import java.io.Serializable;

public class VideoDTOFrontEnd implements Serializable {
    private int stt;
    private int id;
    private String title;
    private String thumnail;
    private String content;
    private Integer numOfView;
    private String coursename;
    private String status;

    public VideoDTOFrontEnd() {
    }

    public String getThumnail() {
        return thumnail;
    }

    public void setThumnail(String thumnail) {
        this.thumnail = thumnail;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCoursename() {
        return coursename;
    }

    public void setCoursename(String coursename) {
        this.coursename = coursename;
    }

    public VideoDTOFrontEnd(int stt, int id, String title, Integer numOfView, String coursename) {
        this.stt = stt;
        this.id = id;
        this.title = title;
        this.numOfView = numOfView;
        this.coursename = coursename;
    }

    public int getStt() {
        return stt;
    }

    public void setStt(int stt) {
        this.stt = stt;
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


}
