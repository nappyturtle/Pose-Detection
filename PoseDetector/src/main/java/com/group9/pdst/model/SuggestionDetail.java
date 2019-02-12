package com.group9.pdst.model;

public class SuggestionDetail {
    private String imgUrl;
    private String standardImgUrl;
    private String description;
    private String suggestionId;

    public SuggestionDetail(String imgUrl, String standardImgUrl, String description, String suggestionId) {
        this.imgUrl = imgUrl;
        this.standardImgUrl = standardImgUrl;
        this.description = description;
        this.suggestionId = suggestionId;
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

    public String getSuggestionId() {
        return suggestionId;
    }

    public void setSuggestionId(String suggestionId) {
        this.suggestionId = suggestionId;
    }

    @Override
    public String toString() {
        return "SuggetionDetail: " +
                "\nSuggestionId:" + suggestionId +
                "\nStandard Image: " + standardImgUrl +
                "\nImage: " + imgUrl +
                "\nDescription: " + description;
    }
}
