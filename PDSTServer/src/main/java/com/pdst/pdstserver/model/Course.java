package com.pdst.pdstserver.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
public class Course implements Serializable {
    private int id;
    private String name;
    private String status;
    private String prevStatus;
    private Integer categoryId;
    private Integer accountId;
    private Integer price;
    private String createdTime;
    private String updatedTime;
    private String thumbnail;
    private Integer videoLimit;
    private Integer enrollmentLimit;

    @Basic
    @Column(name = "thumbnail")
    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

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
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Basic
    @Column(name = "prev_status")
    public String getPrevStatus() {
        return prevStatus;
    }

    public void setPrevStatus(String prevStatus) {
        this.prevStatus = prevStatus;
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
    @Column(name = "account_id")
    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    @Basic
    @Column(name = "price")
    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return id == course.id &&
                Objects.equals(name, course.name) &&
                Objects.equals(categoryId, course.categoryId) &&
                Objects.equals(accountId, course.accountId) &&
                Objects.equals(price, course.price) &&
                Objects.equals(status, course.status) &&
                Objects.equals(prevStatus, course.prevStatus) &&
                Objects.equals(createdTime, course.createdTime) &&
                Objects.equals(updatedTime, course.updatedTime) &&
                Objects.equals(thumbnail, course.thumbnail)
                ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, categoryId, accountId, price, status, createdTime, updatedTime, thumbnail, prevStatus);
    }

    @Basic
    @Column(name = "video_limit")
    public Integer getVideoLimit() {
        return videoLimit;
    }

    public void setVideoLimit(Integer videoLimit) {
        this.videoLimit = videoLimit;
    }

    @Basic
    @Column(name = "enrollment_limit")
    public Integer getEnrollmentLimit() {
        return enrollmentLimit;
    }

    public void setEnrollmentLimit(Integer enrollmentLimit) {
        this.enrollmentLimit = enrollmentLimit;
    }
}
