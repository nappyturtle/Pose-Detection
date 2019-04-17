package com.pdst.pdstserver.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
public class SuggestionTurnTransaction implements Serializable {
    private int id;
    private String createdTime;
    private Integer videoId;
    private Integer accountId;
    private Integer price;
    private Integer suggestionTurn;

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
    @Column(name = "price")
    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    @Basic
    @Column(name = "suggestion_turn")
    public Integer getSuggestionTurn() {
        return suggestionTurn;
    }

    public void setSuggestionTurn(Integer suggestionTurn) {
        this.suggestionTurn = suggestionTurn;
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
        SuggestionTurnTransaction suggestionTurnTransaction = (SuggestionTurnTransaction) o;
        return id == suggestionTurnTransaction.id &&
                Objects.equals(videoId, suggestionTurnTransaction.videoId) &&
                Objects.equals(accountId, suggestionTurnTransaction.accountId) &&
                Objects.equals(price, suggestionTurnTransaction.price) &&
                Objects.equals(suggestionTurn, suggestionTurnTransaction.suggestionTurn) &&
                Objects.equals(createdTime, suggestionTurnTransaction.createdTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, videoId, accountId, price, suggestionTurn, createdTime);
    }
}
