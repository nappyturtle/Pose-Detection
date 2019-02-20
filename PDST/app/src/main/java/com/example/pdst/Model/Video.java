package com.example.pdst.Model;

import java.io.Serializable;
import java.util.Date;

public class Video implements Serializable {
    private int id;
    private String title;
    private String thumbnail_url;
    private String content_url;
    private Account account;
    private Category category;
    private int num_of_view;
    private String status;
    private Date created_time;
    private Date updated_time;
    private String folder_name;

    public Video() {
    }

    public Video(int id, String title, String thumbnail_url, String content_url,
                 Account account, Category category, int num_of_view, String status,
                 Date created_time, Date updated_time, String folder_name) {
        this.id = id;
        this.title = title;
        this.thumbnail_url = thumbnail_url;
        this.content_url = content_url;
        this.account = account;
        this.category = category;
        this.num_of_view = num_of_view;
        this.status = status;
        this.created_time = created_time;
        this.updated_time = updated_time;
        this.folder_name = folder_name;
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

    public String getThumbnail_url() {
        return thumbnail_url;
    }

    public void setThumbnail_url(String thumbnail_url) {
        this.thumbnail_url = thumbnail_url;
    }

    public String getContent_url() {
        return content_url;
    }

    public void setContent_url(String content_url) {
        this.content_url = content_url;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getNum_of_view() {
        return num_of_view;
    }

    public void setNum_of_view(int num_of_view) {
        this.num_of_view = num_of_view;
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

    public String getFolder_name() {
        return folder_name;
    }

    public void setFolder_name(String folder_name) {
        this.folder_name = folder_name;
    }
}
