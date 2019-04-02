package com.pdst.pdstserver.video;
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
    private Integer courseId;
    private Integer numOfView;
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
    @Column(name = "num_of_view")
    public Integer getNumOfView() {
        return numOfView;
    }

    public void setNumOfView(Integer numOfView) {
        this.numOfView = numOfView;
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
    @Column(name = "course_id")
    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
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
                Objects.equals(numOfView, video.numOfView) &&
                Objects.equals(status, video.status) &&
                Objects.equals(createdTime, video.createdTime) &&
                Objects.equals(updatedTime, video.updatedTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, thumnailUrl, contentUrl, numOfView, status, createdTime, updatedTime);
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