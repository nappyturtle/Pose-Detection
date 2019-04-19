package com.pdst.pdstserver.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "upgradecoursetransaction")
public class UpgradeCourseTransaction implements Serializable {
    private int id;
    private String createdTime;
    private Integer courseId;
    private Integer price;
    private Integer videoLimit;
    private Integer enrollmentLimit;

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
    @Column(name = "course_id")
    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
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
    @Column(name = "enrollment_limit")
    public Integer getEnrollmentLimit() {
        return enrollmentLimit;
    }

    public void setEnrollmentLimit(Integer enrollmentLimit) {
        this.enrollmentLimit = enrollmentLimit;
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
    @Column(name = "created_time")
    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UpgradeCourseTransaction upgradeCourseTransaction = (UpgradeCourseTransaction) o;
        return id == upgradeCourseTransaction.id &&
                Objects.equals(courseId, upgradeCourseTransaction.courseId) &&
                Objects.equals(price, upgradeCourseTransaction.price) &&
                Objects.equals(videoLimit, upgradeCourseTransaction.videoLimit) &&
                Objects.equals(enrollmentLimit, upgradeCourseTransaction.enrollmentLimit) &&
                Objects.equals(createdTime, upgradeCourseTransaction.createdTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, courseId, price, videoLimit, enrollmentLimit, createdTime);
    }
}
