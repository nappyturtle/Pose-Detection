package com.pdst.pdstserver.models;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
public class Account {
    private Integer id;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String gender;
    private String imgUrl;
    private String address;
    private Integer numOfFollow;
    private Integer roleId;
    private String status;
    private Timestamp createdTime;
    private Timestamp updatedTime;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "phone")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Basic
    @Column(name = "gender")
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Basic
    @Column(name = "img_url")
    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Basic
    @Column(name = "address")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Basic
    @Column(name = "num_of_follow")
    public Integer getNumOfFollow() {
        return numOfFollow;
    }

    public void setNumOfFollow(Integer numOfFollow) {
        this.numOfFollow = numOfFollow;
    }

    @Basic
    @Column(name = "role_id")
    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
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
    @Column(name = "created_time")
    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    @Basic
    @Column(name = "updated_time")
    public Timestamp getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Timestamp updatedTime) {
        this.updatedTime = updatedTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(id, account.id) &&
                Objects.equals(username, account.username) &&
                Objects.equals(password, account.password) &&
                Objects.equals(email, account.email) &&
                Objects.equals(phone, account.phone) &&
                Objects.equals(gender, account.gender) &&
                Objects.equals(imgUrl, account.imgUrl) &&
                Objects.equals(address, account.address) &&
                Objects.equals(numOfFollow, account.numOfFollow) &&
                Objects.equals(roleId, account.roleId) &&
                Objects.equals(status, account.status) &&
                Objects.equals(createdTime, account.createdTime) &&
                Objects.equals(updatedTime, account.updatedTime);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, username, password, email, phone, gender, imgUrl, address, numOfFollow, roleId, status, createdTime, updatedTime);
    }
}
