package com.group9.pdst.model;

public class MatchingPointResult {
    private String part;
    private double maxMatchingPercentage;
    private double minMatchingPercentage;
    private int weight;
    public MatchingPointResult(String part, double maxMatchingPercentage, double minMatchingPercentage) {
        this.part = part;
        this.maxMatchingPercentage = maxMatchingPercentage;
        this.minMatchingPercentage = minMatchingPercentage;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
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
