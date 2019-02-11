package com.pdst.pdstserver.models;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Objects;

@Entity
public class Suggestion {
    private int id;
    private String name;
    private Integer accountId;
    private Integer videoId;
    private String status;
    private Timestamp createdTime;
    private Timestamp updatedTime;
    private Account accountByAccountId;
    private Video videoByVideoId;
    private Collection<SuggestionDetail> suggestionDetailsById;

    @Id
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
    @Column(name = "account_id")
    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
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
        Suggestion that = (Suggestion) o;
        return id == that.id &&
                Objects.equals(name, that.name) &&
                Objects.equals(accountId, that.accountId) &&
                Objects.equals(videoId, that.videoId) &&
                Objects.equals(status, that.status) &&
                Objects.equals(createdTime, that.createdTime) &&
                Objects.equals(updatedTime, that.updatedTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, accountId, videoId, status, createdTime, updatedTime);
    }

    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id", insertable = false, updatable = false)
    @JsonBackReference
    public Account getAccountByAccountId() {
        return accountByAccountId;
    }

    public void setAccountByAccountId(Account accountByAccountId) {
        this.accountByAccountId = accountByAccountId;
    }

    @ManyToOne
    @JoinColumn(name = "video_id", referencedColumnName = "id", insertable = false, updatable = false)
    @JsonBackReference
    public Video getVideoByVideoId() {
        return videoByVideoId;
    }

    public void setVideoByVideoId(Video videoByVideoId) {
        this.videoByVideoId = videoByVideoId;
    }

    @OneToMany(mappedBy = "suggestionBySuggestionId")
    public Collection<SuggestionDetail> getSuggestionDetailsById() {
        return suggestionDetailsById;
    }

    public void setSuggestionDetailsById(Collection<SuggestionDetail> suggestionDetailsById) {
        this.suggestionDetailsById = suggestionDetailsById;
    }
}
