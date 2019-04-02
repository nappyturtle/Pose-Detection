package com.pdst.pdstserver.enrollment;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
public class Enrollment implements Serializable {
    private int id;
    private Integer courseId;
    private Integer accountId;
    private String createdTime;
    private Integer price;
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
    @Column(name = "price")
    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    @Basic
    @Column(name = "course_Id")
    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
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
    @Column(name = "created_time")
    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accountId, courseId, createdTime, price);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Enrollment that = (Enrollment) o;
        return  id == that.id &&
                Objects.equals(accountId, that.accountId) &&
                Objects.equals(courseId, that.courseId) &&
                Objects.equals(createdTime, that.createdTime) &&
                Objects.equals(price, that.price);
    }
}
