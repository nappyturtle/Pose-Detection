package com.pdst.pdstserver.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "videotransaction")
public class VideoTransaction implements Serializable {
    private int id;
    private Integer videoId;
    private Integer accountId;
    private String createdTime;

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
    @Column(name = "video_id")
    public Integer getVideoId() {
        return videoId;
    }

    public void setVideoId(Integer videoId) {
        this.videoId = videoId;
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
        return Objects.hash(id, accountId, videoId, createdTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VideoTransaction that = (VideoTransaction) o;
        return  id == that.id &&
                Objects.equals(accountId, that.accountId) &&
                Objects.equals(videoId, that.videoId) &&
                Objects.equals(createdTime, that.createdTime);
    }
}
