package com.group9.pdst.model;

public class MatchingPointResult {
    private String part;
    private double xDeviation;
    private double yDeviation;
    private double maxMatchingPercentage;
    private double minMatchingPercentage;
    private int weight;
    public MatchingPointResult(String part, double xDeviation, double yDeviation, double maxMatchingPercentage, double minMatchingPercentage) {
        this.part = part;
        this.xDeviation = xDeviation;
        this.yDeviation = yDeviation;
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

    public void setPart(String part) {
        this.part = part;
    }

    public double getxDeviation() {
        return xDeviation;
    }

    public void setxDeviation(double xDeviation) {
        this.xDeviation = xDeviation;
    }

    public double getyDeviation() {
        return yDeviation;
    }

    public void setyDeviation(double yDeviation) {
        this.yDeviation = yDeviation;
    }

    @Override
    public String toString() {
        return "MatchingPointResult{" +
                "part='" + part + '\'' +
                ", xDeviation=" + xDeviation +
                ", yDeviation=" + yDeviation +
                ", maxMatchingPercentage=" + maxMatchingPercentage +
                ", minMatchingPercentage=" + minMatchingPercentage +
                '}';
    }
}
