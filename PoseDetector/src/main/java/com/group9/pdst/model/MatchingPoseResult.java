package com.group9.pdst.model;

import java.util.ArrayList;
import java.util.List;

public class MatchingPoseResult {
    private String imgUrl;
    private String standardImgUrl;
    private String description;
    private double matchingPercentage;
    public MatchingPoseResult(String imgUrl, String standardImgUrl, String description, double matchingPercentage) {
        this.imgUrl = imgUrl;
        this.standardImgUrl = standardImgUrl;
        this.matchingPercentage = matchingPercentage;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getStandardImgUrl() {
        return standardImgUrl;
    }

    public void setStandardImgUrl(String standardImgUrl) {
        this.standardImgUrl = standardImgUrl;
    }

    public double getMatchingPercentage() {
        return matchingPercentage;
    }

    public void setMatchingPercentage(double matchingPercentage) {
        this.matchingPercentage = matchingPercentage;
    }

}
