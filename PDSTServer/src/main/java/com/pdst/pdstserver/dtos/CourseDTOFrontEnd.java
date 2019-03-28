package com.pdst.pdstserver.dtos;

import com.pdst.pdstserver.models.Category;

import java.io.Serializable;
import java.util.List;

public class CourseDTOFrontEnd implements Serializable {
    private int stt;
    private int id;
    private String coursename;
    private Integer categoryId;
    private String categoryname;
    private String accountname;
    private Integer price;
    private String thumbnail;
    private List<Category> categoryList;
    private String status;

    public CourseDTOFrontEnd(int stt, int id, String coursename, String categoryname, String accountname) {
        this.stt = stt;
        this.id = id;
        this.coursename = coursename;
        this.categoryname = categoryname;
        this.accountname = accountname;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public int getStt() {
        return stt;
    }

    public void setStt(int stt) {
        this.stt = stt;
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    public String getAccountname() {
        return accountname;
    }

    public void setAccountname(String accountname) {
        this.accountname = accountname;
    }

    public CourseDTOFrontEnd() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCoursename() {
        return coursename;
    }

    public void setCoursename(String coursename) {
        this.coursename = coursename;
    }



    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }



    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
