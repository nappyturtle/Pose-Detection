package com.capstone.self_training.dto;

import com.capstone.self_training.model.Account;
import com.capstone.self_training.model.Course;

import java.io.Serializable;
import java.util.List;

public class CourseDTO implements Serializable {
    private Course course;
    private String trainerName;
    private int numberOfRegister;
    private int numberOfVideoInCourse;
    private List<Account> traineeList;

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
