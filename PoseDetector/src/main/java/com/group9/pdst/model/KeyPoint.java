/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.group9.pdst.model;

/**
 *
 * @author VU
 */
public class KeyPoint {
    private double score;
    private Position position;
    private String part;

    public KeyPoint(double score, Position position, String part) {
        this.score = score;
        this.position = position;
        this.part = part;
    }

    public KeyPoint() {
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    @Override
    public String toString() {
        return "KeyPoint{" + "score=" + score + ", x=" + position.getX() + ", y=" + position.getY() + ", part=" + part + '}';
    }
    
}
