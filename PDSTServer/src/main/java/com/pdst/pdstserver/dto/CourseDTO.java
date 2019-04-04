package com.pdst.pdstserver.dto;

import com.pdst.pdstserver.model.Account;
import com.pdst.pdstserver.model.Course;
import com.pdst.pdstserver.model.Video;

import java.io.Serializable;
import java.util.List;

public class CourseDTO implements Serializable {
    private Course course;
    private String trainerName;
    private int numberOfRegister;
    private int numberOfVideoInCourse;
    private List<Account> traineeList;
    private List<Video> videoUpdated;

    public List<Video> getVideoUpdated() {
        return videoUpdated;
    }

    public void setVideoUpdated(List<Video> videoUpdated) {
        this.videoUpdated = videoUpdated;
    }

    public List<Account> getTraineeList() {
        return traineeList;
    }

    public void setTraineeList(List<Account> traineeList) {
        this.traineeList = traineeList;
    }

    public int getNumberOfVideoInCourse() {
        return numberOfVideoInCourse;
    }

    public void setNumberOfVideoInCourse(int numberOfVideoInCourse) {
        this.numberOfVideoInCourse = numberOfVideoInCourse;
    }

    public CourseDTO() {
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getTrainerName() {
        return trainerName;
    }

    public void setTrainerName(String trainerName) {
        this.trainerName = trainerName;
    }

    public int getNumberOfRegister() {
        return numberOfRegister;
    }

    public void setNumberOfRegister(int numberOfRegister) {
        this.numberOfRegister = numberOfRegister;
    }
}
