package com.capstone.self_training.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SuggestionDetail implements Serializable {


    private int id;
    private String imgUrl;
    private String imgStandardUrl;
    private String result;



    public SuggestionDetail(int id, String imgUrl, String imgStandardUrl, String result) {
      this.setId(id);
      this.setImgUrl(imgUrl);
      this.setImgStandardUrl(imgStandardUrl);
      this.setResult(result);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getImgStandardUrl() {
        return imgStandardUrl;
    }

    public void setImgStandardUrl(String imgStandardUrl) {
        this.imgStandardUrl = imgStandardUrl;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
