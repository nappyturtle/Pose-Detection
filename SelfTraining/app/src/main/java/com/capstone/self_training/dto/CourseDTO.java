package com.capstone.self_training.dto;

import com.capstone.self_training.model.Course;

import java.io.Serializable;

public class CourseDTO implements Serializable {
    private Course course;
    private String trainerName;
    private int numberOfRegister;
    private int numberOfVideoInCourse;

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
