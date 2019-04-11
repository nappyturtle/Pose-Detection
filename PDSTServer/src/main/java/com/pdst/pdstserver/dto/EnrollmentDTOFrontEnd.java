package com.pdst.pdstserver.dto;

import com.pdst.pdstserver.model.Enrollment;

public class EnrollmentDTOFrontEnd {
    private Enrollment enrollment;
    private String coursename;
    private String username;

    public EnrollmentDTOFrontEnd() {
    }

    public Enrollment getEnrollment() {
        return enrollment;
    }

    public void setEnrollment(Enrollment enrollment) {
        this.enrollment = enrollment;
    }

    public String getCoursename() {
        return coursename;
    }

    public void setCoursename(String coursename) {
        this.coursename = coursename;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
