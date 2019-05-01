package com.pdst.pdstserver.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
@Entity
public class Frame implements Serializable {
    private int id;
    private Integer videoId;
    private String url;
    private Integer noseWeight;
    private Integer leftShoulderWeight;
    private Integer rightShoulderWeight;
    private Integer leftElbowWeight;
    private Integer rightElbowWeight;
    private Integer leftWristWeight;
    private Integer rightWristWeight;
    private Integer leftKneeWeight;
    private Integer rightKneeWeight;
    private Integer leftAnkleWeight;
    private Integer rightAnkleWeight;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "video_id")
    public Integer getVideoId() {
        return videoId;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Basic
    @Column(name = "url")
    public String getUrl() {
        return url;
    }
    public void setVideoId(Integer videoId) {
        this.videoId = videoId;
    }

    @Basic
    @Column(name = "nose_weight")
    public Integer getNoseWeight() {
        return noseWeight;
    }

    public void setNoseWeight(Integer noseWeight) {
        this.noseWeight = noseWeight;
    }

    @Basic
    @Column(name = "left_shoulder_weight")
    public Integer getLeftShoulderWeight() {
        return leftShoulderWeight;
    }

    public void setLeftShoulderWeight(Integer leftShoulderWeight) {
        this.leftShoulderWeight = leftShoulderWeight;
    }

    @Basic
    @Column(name = "right_shoulder_weight")
    public Integer getRightShoulderWeight() {
        return rightShoulderWeight;
    }

    public void setRightShoulderWeight(Integer rightShoulderWeight) {
        this.rightShoulderWeight = rightShoulderWeight;
    }

    @Basic
    @Column(name = "left_elbow_weight")
    public Integer getLeftElbowWeight() {
        return leftElbowWeight;
    }

    public void setLeftElbowWeight(Integer leftElbowWeight) {
        this.leftElbowWeight = leftElbowWeight;
    }

    @Basic
    @Column(name = "right_elbow_weight")
    public Integer getRightElbowWeight() {
        return rightElbowWeight;
    }

    public void setRightElbowWeight(Integer rightElbowWeight) {
        this.rightElbowWeight = rightElbowWeight;
    }

    @Basic
    @Column(name = "left_wrist_weight")
    public Integer getLeftWristWeight() {
        return leftWristWeight;
    }

    public void setLeftWristWeight(Integer leftWristWeight) {
        this.leftWristWeight = leftWristWeight;
    }

    @Basic
    @Column(name = "right_wrist_weight")
    public Integer getRightWristWeight() {
        return rightWristWeight;
    }

    public void setRightWristWeight(Integer rightWristWeight) {
        this.rightWristWeight = rightWristWeight;
    }

    @Basic
    @Column(name = "left_knee_weight")
    public Integer getLeftKneeWeight() {
        return leftKneeWeight;
    }

    public void setLeftKneeWeight(Integer leftKneeWeight) {
        this.leftKneeWeight = leftKneeWeight;
    }

    @Basic
    @Column(name = "right_knee_weight")
    public Integer getRightKneeWeight() {
        return rightKneeWeight;
    }

    public void setRightKneeWeight(Integer rightKneeWeight) {
        this.rightKneeWeight = rightKneeWeight;
    }

    @Basic
    @Column(name = "left_ankle_weight")
    public Integer getLeftAnkleWeight() {
        return leftAnkleWeight;
    }

    public void setLeftAnkleWeight(Integer leftAnkleWeight) {
        this.leftAnkleWeight = leftAnkleWeight;
    }

    @Basic
    @Column(name = "right_ankle_weight")
    public Integer getRightAnkleWeight() {
        return rightAnkleWeight;
    }

    public void setRightAnkleWeight(Integer rightAnkleWeight) {
        this.rightAnkleWeight = rightAnkleWeight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Frame that = (Frame) o;
        return id == that.id &&
                Objects.equals(url, that.url) &&
                Objects.equals(videoId, that.videoId) &&
                Objects.equals(noseWeight, that.noseWeight) &&
                Objects.equals(leftShoulderWeight, that.leftShoulderWeight) &&
                Objects.equals(rightShoulderWeight, that.rightShoulderWeight) &&
                Objects.equals(leftElbowWeight, that.leftElbowWeight) &&
                Objects.equals(rightElbowWeight, that.rightElbowWeight) &&
                Objects.equals(leftWristWeight, that.leftWristWeight) &&
                Objects.equals(rightWristWeight, that.rightWristWeight) &&
                Objects.equals(leftKneeWeight, that.leftKneeWeight) &&
                Objects.equals(rightKneeWeight, that.rightKneeWeight) &&
                Objects.equals(leftAnkleWeight, that.leftAnkleWeight) &&
                Objects.equals(rightAnkleWeight, that.rightAnkleWeight);
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, url, videoId, noseWeight, leftShoulderWeight, rightShoulderWeight, leftElbowWeight, rightElbowWeight, leftWristWeight, rightWristWeight, leftKneeWeight, rightKneeWeight, leftAnkleWeight, rightAnkleWeight);
    }
}
