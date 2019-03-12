package com.group9.pdst.model;

import java.io.Serializable;

public class MatchingPointResult implements Serializable{
    private String part;
    private double maxMatchingPercentage;
    private double minMatchingPercentage;
    public MatchingPointResult(String part, double maxMatchingPercentage, double minMatchingPercentage) {
        this.part = part;
        this.maxMatchingPercentage = maxMatchingPercentage;
        this.minMatchingPercentage = minMatchingPercentage;
    }

    public MatchingPointResult() {
    }

    public double getMaxMatchingPercentage() {
        return maxMatchingPercentage;
    }

    public void setMaxMatchingPercentage(double maxMatchingPercentage) {
        this.maxMatchingPercentage = maxMatchingPercentage;
    }

    public double getMinMatchingPercentage() {
        return minMatchingPercentage;
    }

    public void setMinMatchingPercentage(double minMatchingPercentage) {
        this.minMatchingPercentage = minMatchingPercentage;
    }

    public String getPart() {
        return part;
    }


    @Override
    public String toString() {
        return "MatchingPointResult{" +
                "part='" + part + '\'' +
                ", maxMatchingPercentage=" + maxMatchingPercentage +
                ", minMatchingPercentage=" + minMatchingPercentage +
                '}';
    }
}
