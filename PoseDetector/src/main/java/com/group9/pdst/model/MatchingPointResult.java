package com.group9.pdst.model;

public class MatchingPointResult {
    private String part;
    private double xDeviation;
    private double yDeviation;
    private double matchingPercentage;

    public MatchingPointResult(String part, double xDeviation, double yDeviation, double matchingPercentage) {
        this.part = part;
        this.xDeviation = xDeviation;
        this.yDeviation = yDeviation;
        this.matchingPercentage = matchingPercentage;
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

    public double getMatchingPercentage() {
        return matchingPercentage;
    }

    public void setMatchingPercentage(double matchingPercentage) {
        this.matchingPercentage = matchingPercentage;
    }

    @Override
    public String toString() {
        return "MatchingPointResult{" +
                "part='" + part + '\'' +
                ", xDeviation=" + xDeviation +
                ", yDeviation=" + yDeviation +
                ", matchingPercentage=" + matchingPercentage +
                '}';
    }
}
