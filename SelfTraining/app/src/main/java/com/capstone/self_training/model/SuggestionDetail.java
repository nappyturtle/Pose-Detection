package com.capstone.self_training.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SuggestionDetail implements Serializable{

@SerializedName("id")
@Expose
private Integer id;
@SerializedName("imgUrl")
@Expose
private String imgUrl;
@SerializedName("standardImgUrl")
@Expose
private String standardImgUrl;
@SerializedName("description")
@Expose
private String description;
@SerializedName("suggestionId")
@Expose
private Integer suggestionId;
@SerializedName("status")
@Expose
private String status;
@SerializedName("createdTime")
@Expose
private String createdTime;
@SerializedName("updatedTime")
@Expose
private Object updatedTime;

public Integer getId() {
return id;
}

public void setId(Integer id) {
this.id = id;
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

public Integer getSuggestionId() {
return suggestionId;
}

public void setSuggestionId(Integer suggestionId) {
this.suggestionId = suggestionId;
}

public String getStatus() {
return status;
}

public void setStatus(String status) {
this.status = status;
}

public String getCreatedTime() {
return createdTime;
}

public void setCreatedTime(String createdTime) {
this.createdTime = createdTime;
}

public Object getUpdatedTime() {
return updatedTime;
}

public void setUpdatedTime(Object updatedTime) {
this.updatedTime = updatedTime;
}

}