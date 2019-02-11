package com.pdst.pdstserver.models;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
public class SuggestionDetail {
    private int id;
    private String imgUrl;
    private String standardImgUrl;
    private String description;
    private Integer suggestionId;
    private String status;
    private Timestamp createdTime;
    private Timestamp updatedTime;
    private Suggestion suggestionBySuggestionId;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
    @Column(name = "standard_img_url")
    public String getStandardImgUrl() {
        return standardImgUrl;
    }

    public void setStandardImgUrl(String standardImgUrl) {
        this.standardImgUrl = standardImgUrl;
    }

    @Basic
    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "suggestion_id")
    public Integer getSuggestionId() {
        return suggestionId;
    }

    public void setSuggestionId(Integer suggestionId) {
        this.suggestionId = suggestionId;
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
        SuggestionDetail that = (SuggestionDetail) o;
        return id == that.id &&
                Objects.equals(imgUrl, that.imgUrl) &&
                Objects.equals(standardImgUrl, that.standardImgUrl) &&
                Objects.equals(description, that.description) &&
                Objects.equals(suggestionId, that.suggestionId) &&
                Objects.equals(status, that.status) &&
                Objects.equals(createdTime, that.createdTime) &&
                Objects.equals(updatedTime, that.updatedTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, imgUrl, standardImgUrl, description, suggestionId, status, createdTime, updatedTime);
    }

    @ManyToOne
    @JoinColumn(name = "suggestion_id", referencedColumnName = "id", insertable = false, updatable = false)
    @JsonBackReference
    public Suggestion getSuggestionBySuggestionId() {
        return suggestionBySuggestionId;
    }

    public void setSuggestionBySuggestionId(Suggestion suggestionBySuggestionId) {
        this.suggestionBySuggestionId = suggestionBySuggestionId;
    }
}
