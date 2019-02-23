package com.group9.pdst.model;

import java.util.ArrayList;
import java.util.List;

public class MatchingPoseResult {
    private String imgUrl;
    private String standardImgUrl;
    private String description;
    private double maxMatchingPercentage;
    private double minMatchingPercentage;
    public MatchingPoseResult(String imgUrl, String standardImgUrl, String description, double maxMatchingPercentage, double minMatchingPercentage) {
        this.imgUrl = imgUrl;
        this.standardImgUrl = standardImgUrl;
        this.description = description;
        this.maxMatchingPercentage = maxMatchingPercentage;
        this.minMatchingPercentage = minMatchingPercentage;
    }

    public double getMaxMatchingPercentage() {
        return maxMatchingPercentage;
    }

    public void setMaxMatchingPercentage(double maxMatchingPercentage) {
        this.maxMatchingPercentage = maxMatchingPercentage;
    }

    public double getMinMatchingPercentage() {
        return minMatchingPercentage;
    }

    public void setMinMatchingPercentage(double minMatchingPercentage) {
        this.minMatchingPercentage = minMatchingPercentage;
    }

    public MatchingPoseResult() {
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

    @Override
    public String toString() {
        return "MatchingPoseResult{" +
                "imgUrl='" + imgUrl + '\'' +
                ", standardImgUrl='" + standardImgUrl + '\'' +
                ", description='" + description + '\'' +
                ", maxMatchingPercentage=" + maxMatchingPercentage +
                ", minMatchingPercentage=" + minMatchingPercentage +
                '}';
    }
}
