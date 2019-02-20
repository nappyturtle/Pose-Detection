package com.example.pdst.Model;

import java.io.Serializable;
import java.util.Date;

public class Category implements Serializable {
    private int id;
    private String name;
    private String status;
    private Date created_time;
    private Date updated_time;

    public Category(int id, String name, String status, Date created_time, Date updated_time) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.created_time = created_time;
        this.updated_time = updated_time;
    }

    public Category() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreated_time() {
        return created_time;
    }

    public void setCreated_time(Date created_time) {
        this.created_time = created_time;
    }

    public Date getUpdated_time() {
        return updated_time;
    }

    public void setUpdated_time(Date updated_time) {
        this.updated_time = updated_time;
    }
}
