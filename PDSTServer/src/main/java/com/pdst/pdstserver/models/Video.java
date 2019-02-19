package com.pdst.pdstserver.models;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.*;
import java.io.*;
import java.util.Objects;

@Entity
public class Video implements Serializable {
    private int id;
    private String title;
    private String thumnailUrl;
    private String contentUrl;
    private Integer accountId;
    private Integer categoryId;
    private Integer numOfView;
    private Integer headWeight;
    private Integer bodyWeight;
    private Integer legWeight;
    private String status;

    private String folderName;
    private String createdTime;
    private String updatedTime;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "thumnail_url")
    public String getThumnailUrl() {
        return thumnailUrl;
    }

    public void setThumnailUrl(String thumnailUrl) {
        this.thumnailUrl = thumnailUrl;
    }

    @Basic
    @Column(name = "content_url")
    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    @Basic
    @Column(name = "account_id")
    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    @Basic
    @Column(name = "category_id")
    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    @Basic
    @Column(name = "num_of_view")
    public Integer getNumOfView() {
        return numOfView;
    }

    public void setNumOfView(Integer numOfView) {
        this.numOfView = numOfView;
    }
    @Basic
    @Column(name = "head_weight")
    public Integer getHeadWeight() {
        return headWeight;
    }

    public void setHeadWeight(Integer headWeight) {
        this.headWeight = headWeight;
    }
    @Basic
    @Column(name = "body_head")
    public Integer getBodyWeight() {
        return bodyWeight;
    }

    public void setBodyWeight(Integer bodyWeight) {
        this.bodyWeight = bodyWeight;
    }
    @Basic
    @Column(name = "leg_weight")
    public Integer getLegWeight() {
        return legWeight;
    }

    public void setLegWeight(Integer legWeight) {
        this.legWeight = legWeight;
    }

    @Basic
    @Column(name = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Video video = (Video) o;
        return id == video.id &&
                Objects.equals(title, video.title) &&
                Objects.equals(thumnailUrl, video.thumnailUrl) &&
                Objects.equals(contentUrl, video.contentUrl) &&
                Objects.equals(accountId, video.accountId) &&
                Objects.equals(categoryId, video.categoryId) &&
                Objects.equals(numOfView, video.numOfView) &&
                Objects.equals(status, video.status) &&
                Objects.equals(createdTime, video.createdTime) &&
                Objects.equals(updatedTime, video.updatedTime) &&
                Objects.equals(headWeight, video.headWeight) &&
                Objects.equals(bodyWeight, video.bodyWeight) &&
                Objects.equals(legWeight, video.legWeight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, thumnailUrl, contentUrl, accountId, categoryId, numOfView, status, createdTime, updatedTime, headWeight, bodyWeight, legWeight);
    }

    @Basic
    @Column(name = "folder_name")
    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    @Basic
    @Column(name = "created_time")
    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    @Basic
    @Column(name = "updated_time")
    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }
}