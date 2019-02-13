package com.group9.pdst.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public class FileInfo {
	private String filename;
	private String content;
	private String videoName;
	private String videoUrl;
	private String foldername;

	public String getFoldername() {
		return foldername;
	}

	public void setFoldername(String foldername) {
		this.foldername = foldername;
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