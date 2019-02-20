package com.capstone.self_training.model;

import java.io.Serializable;

public class Video implements Serializable{
    private int id;
    private String title;
    private String thumnailUrl;
    private String contentUrl;
    private Integer accountId;
    private Integer categoryId;
    private Integer numOfView;
    private String status;
    private String username;
    private String categoryName;
    private String folderName;
    private String createdTime;
    private String updatedTime;
    private Integer headWeight;
    private Integer bodyWeight;
    private Integer legWeight;

    public Video(int id, String title, String thumnailUrl, String contentUrl, Integer accountId, Integer categoryId, Integer numOfView, String status, String username, String categoryName, String folderName, String createdTime, String updatedTime, Integer headWeight, Integer bodyWeight, Integer legWeight) {
        this.id = id;
        this.title = title;
        this.thumnailUrl = thumnailUrl;
        this.contentUrl = contentUrl;
        this.accountId = accountId;
        this.categoryId = categoryId;
        this.numOfView = numOfView;
        this.status = status;
        this.username = username;
        this.categoryName = categoryName;
        this.folderName = folderName;
        this.createdTime = createdTime;
        this.updatedTime = updatedTime;
        this.headWeight = headWeight;
        this.bodyWeight = bodyWeight;
        this.legWeight = legWeight;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public void setNumOfView(Integer numOfView) {
        this.numOfView = numOfView;
    }

    public Integer getHeadWeight() {
        return headWeight;
    }

    public void setHeadWeight(Integer headWeight) {
        this.headWeight = headWeight;
    }

    public Integer getBodyWeight() {
        return bodyWeight;
    }

    public void setBodyWeight(Integer bodyWeight) {
        this.bodyWeight = bodyWeight;
    }

    public Integer getLegWeight() {
        return legWeight;
    }

    public void setLegWeight(Integer legWeight) {
        this.legWeight = legWeight;
    }

    public Video(){}

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

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getNumOfView() {
        return numOfView;
    }

    public void setNumOfView(int numOfView) {
        this.numOfView = numOfView;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
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
