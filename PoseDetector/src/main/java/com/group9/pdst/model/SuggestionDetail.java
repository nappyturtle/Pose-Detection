package com.group9.pdst.model;

public class SuggestionDetail {
    private String imgUrl;
    private String standardImgUrl;
    private String description;
    private double matchingPerentage;

    public SuggestionDetail(String imgUrl, String standardImgUrl, String description, double matchingPerentage) {
        this.imgUrl = imgUrl;
        this.standardImgUrl = standardImgUrl;
        this.description = description;
        this.matchingPerentage = matchingPerentage;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getMatchingPerentage() {
        return matchingPerentage;
    }

    public void setMatchingPerentage(double matchingPerentage) {
        this.matchingPerentage = matchingPerentage;
    }
}
