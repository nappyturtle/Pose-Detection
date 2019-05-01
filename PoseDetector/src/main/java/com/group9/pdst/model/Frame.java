package com.group9.pdst.model;

import java.io.Serializable;

public class Frame implements Serializable {
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

    public Frame(Integer videoId, String url, Integer noseWeight, Integer leftShoulderWeight, Integer rightShoulderWeight, Integer leftElbowWeight, Integer rightElbowWeight, Integer leftWristWeight, Integer rightWristWeight, Integer leftKneeWeight, Integer rightKneeWeight, Integer leftAnkleWeight, Integer rightAnkleWeight) {
        this.videoId = videoId;
        this.url = url;
        this.noseWeight = noseWeight;
        this.leftShoulderWeight = leftShoulderWeight;
        this.rightShoulderWeight = rightShoulderWeight;
        this.leftElbowWeight = leftElbowWeight;
        this.rightElbowWeight = rightElbowWeight;
        this.leftWristWeight = leftWristWeight;
        this.rightWristWeight = rightWristWeight;
        this.leftKneeWeight = leftKneeWeight;
        this.rightKneeWeight = rightKneeWeight;
        this.leftAnkleWeight = leftAnkleWeight;
        this.rightAnkleWeight = rightAnkleWeight;
    }

    public int calculateTotalWeight() {
        int sum = noseWeight + leftShoulderWeight + rightShoulderWeight + leftElbowWeight + rightElbowWeight + leftWristWeight + rightWristWeight + leftKneeWeight + rightKneeWeight + leftAnkleWeight + rightAnkleWeight;
        return sum;
    }
    public void assignWeight(String part, int weight) {
        switch (part) {
            case "nose":
                setNoseWeight(weight);
                break;
            case "leftShoulder":
                setLeftShoulderWeight(weight);
                break;
            case "rightShoulder":
                setRightShoulderWeight(weight);
                break;
            case "leftElbow":
                setLeftElbowWeight(weight);
                break;
            case "rightElbow":
                setRightElbowWeight(weight);
                break;
            case "leftWrist":
                setLeftWristWeight(weight);
                break;
            case "rightWrist":
                setRightWristWeight(weight);
                break;
            case "leftKnee":
                setLeftKneeWeight(weight);
                break;
            case "rightKnee":
                setRightKneeWeight(weight);
                break;
            case "leftAnkle":
                setLeftAnkleWeight(weight);
                break;
            case "rightAnkle":
                setRightAnkleWeight(weight);
                break;
        }
    }

    public int returnWeight(String part) {
        switch (part) {
            case "nose":
                return noseWeight;
            case "leftShoulder":
                return leftShoulderWeight;
            case "rightShoulder":
                return rightShoulderWeight;
            case "leftElbow":
                return leftElbowWeight;
            case "rightElbow":
                return rightElbowWeight;
            case "leftWrist":
                return leftWristWeight;
            case "rightWrist":
                return rightWristWeight;
            case "leftKnee":
                return leftKneeWeight;
            case "rightKnee":
                return rightKneeWeight;
            case "leftAnkle":
                return leftAnkleWeight;
            case "rightAnkle":
                return rightAnkleWeight;
        }
        return 0;
    }
    public Frame() {
    }

    public Integer getVideoId() {
        return videoId;
    }

    public void setVideoId(Integer videoId) {
        this.videoId = videoId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getNoseWeight() {
        return noseWeight;
    }

    public void setNoseWeight(Integer noseWeight) {
        this.noseWeight = noseWeight;
    }

    public Integer getLeftShoulderWeight() {
        return leftShoulderWeight;
    }

    public void setLeftShoulderWeight(Integer leftShoulderWeight) {
        this.leftShoulderWeight = leftShoulderWeight;
    }

    public Integer getRightShoulderWeight() {
        return rightShoulderWeight;
    }

    public void setRightShoulderWeight(Integer rightShoulderWeight) {
        this.rightShoulderWeight = rightShoulderWeight;
    }

    public Integer getLeftElbowWeight() {
        return leftElbowWeight;
    }

    public void setLeftElbowWeight(Integer leftElbowWeight) {
        this.leftElbowWeight = leftElbowWeight;
    }

    public Integer getRightElbowWeight() {
        return rightElbowWeight;
    }

    public void setRightElbowWeight(Integer rightElbowWeight) {
        this.rightElbowWeight = rightElbowWeight;
    }

    public Integer getLeftWristWeight() {
        return leftWristWeight;
    }

    public void setLeftWristWeight(Integer leftWristWeight) {
        this.leftWristWeight = leftWristWeight;
    }

    public Integer getRightWristWeight() {
        return rightWristWeight;
    }

    public void setRightWristWeight(Integer rightWristWeight) {
        this.rightWristWeight = rightWristWeight;
    }

    public Integer getLeftKneeWeight() {
        return leftKneeWeight;
    }

    public void setLeftKneeWeight(Integer leftKneeWeight) {
        this.leftKneeWeight = leftKneeWeight;
    }

    public Integer getRightKneeWeight() {
        return rightKneeWeight;
    }

    public void setRightKneeWeight(Integer rightKneeWeight) {
        this.rightKneeWeight = rightKneeWeight;
    }

    public Integer getLeftAnkleWeight() {
        return leftAnkleWeight;
    }

    public void setLeftAnkleWeight(Integer leftAnkleWeight) {
        this.leftAnkleWeight = leftAnkleWeight;
    }

    public Integer getRightAnkleWeight() {
        return rightAnkleWeight;
    }

    public void setRightAnkleWeight(Integer rightAnkleWeight) {
        this.rightAnkleWeight = rightAnkleWeight;
    }
}
