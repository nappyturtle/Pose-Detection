package com.group9.pdst.model;

public class KeyPointVector extends KeyPoint {
    public KeyPointVector(Position position, String part) {
        this.setPosition(position);
        this.setPart(part);
    }
}
