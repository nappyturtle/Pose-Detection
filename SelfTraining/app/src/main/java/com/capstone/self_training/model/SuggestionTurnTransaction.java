package com.capstone.self_training.model;

import java.io.Serializable;

public class SuggestionTurnTransaction implements Serializable {
    private int id;
    private String createdTime;
    private Integer videoId;
    private Integer accountId;
    private Integer price;
    private Integer suggestionTurn;

    public SuggestionTurnTransaction() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public Integer getVideoId() {
        return videoId;
    }

    public void setVideoId(Integer videoId) {
        this.videoId = videoId;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getSuggestionTurn() {
        return suggestionTurn;
    }

    public void setSuggestionTurn(Integer suggestionTurn) {
        this.suggestionTurn = suggestionTurn;
    }
}
