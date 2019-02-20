package com.group9.pdst.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public class FileInfo {
	private String filename;
	private String content;
	private String videoName;
	private String videoUrl;
	private String foldernameTrainer;
	private String foldernameTrainee;
	private String suggestionId;
	private String foldername;
	private Integer headWeight;
	private Integer bodyWeight;
	private Integer legWeight;

	public Integer getHeadWeight() {
		return headWeight;
	}

	public void setHeadWeight(Integer headWeight) {
		this.headWeight = headWeight;
	}

	public Integer getBodyWeight() {
		return bodyWeight;
	}

	public void setBodyWeight(Integer bodyWeight) {
		this.bodyWeight = bodyWeight;
	}

	public Integer getLegWeight() {
		return legWeight;
	}

	public void setLegWeight(Integer legWeight) {
		this.legWeight = legWeight;
	}

	public String getFoldername() {
		return foldername;
	}

	public void setFoldername(String foldername) {
		this.foldername = foldername;
	}

	public String getSuggestionId() {
		return suggestionId;
	}

	public void setSuggestionId(String suggestionId) {
		this.suggestionId = suggestionId;
	}

	public String getFoldernameTrainee() {
		return foldernameTrainee;
	}

	public void setFoldernameTrainee(String foldernameTrainee) {
		this.foldernameTrainee = foldernameTrainee;
	}

	public String getFoldernameTrainer() {
		return foldernameTrainer;
	}

	public void setFoldernameTrainer(String foldernameTrainer) {
		this.foldernameTrainer = foldernameTrainer;
	}

	public FileInfo(String videoName, String videoUrl) {
		this.videoName = videoName;
		this.videoUrl = videoUrl;
	}

	public String getVideoName() {
		return videoName;
	}

	public void setVideoName(String videoName) {
		this.videoName = videoName;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public FileInfo(){}


	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getFilename() {
		return this.filename;
	}
	
	public void setFilename(String filename) {
		this.filename = filename;
	}

}