package com.capstone.self_training.model;

import java.io.Serializable;

public class Account implements Serializable{
    private int id;
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
    private String createdTime;
    private String updatedTime;

    public Account(){

    }

    public Account(int id, String username, String password, String email, String phone, String gender, String imgUrl, String address, Integer numOfFollow, Integer roleId, String status, String createdTime, String updatedTime) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.imgUrl = imgUrl;
        this.address = address;
        this.numOfFollow = numOfFollow;
        this.roleId = roleId;
        this.status = status;
        this.createdTime = createdTime;
        this.updatedTime = updatedTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getNumOfFollow() {
        return numOfFollow;
    }

    public void setNumOfFollow(Integer numOfFollow) {
        this.numOfFollow = numOfFollow;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
