package com.group9.pdst.utils;

import com.group9.pdst.model.KeyPoint;
import com.group9.pdst.model.KeyPointVector;
import com.group9.pdst.model.MatchingPointResult;
import com.group9.pdst.model.Position;

import java.util.List;

public class TransUtilities {
    public static int headWeight;
    public static int bodyWeight;
    public static int legWeight;
    public static String transPartToVietnamese(String part) {
        switch (part) {
            case "nose":
                return "Đầu";
            case "leftShoulder":
                return "Vai trái";
            case "rightShoulder":
                return "Vai phải";
            case "leftElbow":
                return "Khuỷa tay trái";
            case "rightElbow":
                return "Khuỷa tay phải";
            case "leftWrist":
                return "Cổ tay trái";
            case "rightWrist":
                return "Cổ tay phải";
            case "torso":
                return "Thân";
            case "leftKnee":
                return "Đầu gối trái";
            case "rightKnee":
                return "Đầu gối phải";
            case "leftAnkle":
                return "Mắt cá chân trái";
            case "rightAnkle":
                return "Mắt cá chân phải";
        }
        return "";
    }
    public static void setPartWeight (MatchingPointResult matchingPointResult) {
        switch (matchingPointResult.getPart()) {
            case "nose":
                matchingPointResult.setWeight(headWeight);
                return;
            case "leftShoulder":
                matchingPointResult.setWeight(bodyWeight);
                return;
            case "rightShoulder":
                matchingPointResult.setWeight(bodyWeight);
                return;
            case "leftElbow":
                matchingPointResult.setWeight(bodyWeight);
                return;
            case "rightElbow":
                matchingPointResult.setWeight(bodyWeight);
                return;
            case "leftWrist":
                matchingPointResult.setWeight(bodyWeight);
                return;
            case "rightWrist":
                matchingPointResult.setWeight(bodyWeight);
                return;
            case "torso":
                matchingPointResult.setWeight(bodyWeight);
                return;
            case "leftKnee":
                matchingPointResult.setWeight(legWeight);
                return;
            case "rightKnee":
                matchingPointResult.setWeight(legWeight);
                return;
            case "leftAnkle":
                matchingPointResult.setWeight(legWeight);
                return;
            case "rightAnkle":
                matchingPointResult.setWeight(legWeight);
                return;
        }
        return;
    }
    private static KeyPoint getRootPoint(List<KeyPoint> keyPointList, String rootPointPart) {
        for (int i = 0; i < keyPointList.size(); i++) {
            KeyPoint rootPoint =  keyPointList.get(i);
            if(rootPoint.getPart().equals(rootPointPart)) return rootPoint;
        }
        return null;
    }
    public static KeyPointVector createVectorFromKeypoint(List<KeyPoint> keyPointList, KeyPoint keyPoint) {
        KeyPoint rootPoint;
        Position position;
        KeyPointVector vector;
        switch (keyPoint.getPart()) {
            case "nose":
                rootPoint = getRootPoint(keyPointList, "torso");
                position = new Position(keyPoint.getPosition().getX() - rootPoint.getPosition().getX(), keyPoint.getPosition().getY() - rootPoint.getPosition().getY());
                vector = new KeyPointVector(position, keyPoint.getPart());
                return vector;
            case "leftShoulder":
                rootPoint = getRootPoint(keyPointList, "torso");
                position = new Position(keyPoint.getPosition().getX() - rootPoint.getPosition().getX(), keyPoint.getPosition().getY() - rootPoint.getPosition().getY());
                vector = new KeyPointVector(position, keyPoint.getPart());
                return vector;
            case "rightShoulder":
                rootPoint = getRootPoint(keyPointList, "torso");
                position = new Position(keyPoint.getPosition().getX() - rootPoint.getPosition().getX(), keyPoint.getPosition().getY() - rootPoint.getPosition().getY());
                vector = new KeyPointVector(position, keyPoint.getPart());
                return vector;
            case "leftElbow":
                rootPoint = getRootPoint(keyPointList, "leftShoulder");
                position = new Position(keyPoint.getPosition().getX() - rootPoint.getPosition().getX(), keyPoint.getPosition().getY() - rootPoint.getPosition().getY());
                vector = new KeyPointVector(position, keyPoint.getPart());
                return vector;
            case "rightElbow":
                rootPoint = getRootPoint(keyPointList, "rightShoulder");
                position = new Position(keyPoint.getPosition().getX() - rootPoint.getPosition().getX(), keyPoint.getPosition().getY() - rootPoint.getPosition().getY());
                vector = new KeyPointVector(position, keyPoint.getPart());
                return vector;
            case "leftWrist":
                rootPoint = getRootPoint(keyPointList, "leftElbow");
                position = new Position(keyPoint.getPosition().getX() - rootPoint.getPosition().getX(), keyPoint.getPosition().getY() - rootPoint.getPosition().getY());
                vector = new KeyPointVector(position, keyPoint.getPart());
                return vector;
            case "rightWrist":
                rootPoint = getRootPoint(keyPointList, "rightElbow");
                position = new Position(keyPoint.getPosition().getX() - rootPoint.getPosition().getX(), keyPoint.getPosition().getY() - rootPoint.getPosition().getY());
                vector = new KeyPointVector(position, keyPoint.getPart());
                return vector;
            case "leftKnee":
                rootPoint = getRootPoint(keyPointList, "torso");
                position = new Position(keyPoint.getPosition().getX() - rootPoint.getPosition().getX(), keyPoint.getPosition().getY() - rootPoint.getPosition().getY());
                vector = new KeyPointVector(position, keyPoint.getPart());
                return vector;
            case "rightKnee":
                rootPoint = getRootPoint(keyPointList, "torso");
                position = new Position(keyPoint.getPosition().getX() - rootPoint.getPosition().getX(), keyPoint.getPosition().getY() - rootPoint.getPosition().getY());
                vector = new KeyPointVector(position, keyPoint.getPart());
                return vector;
            case "leftAnkle":
                rootPoint = getRootPoint(keyPointList, "leftKnee");
                position = new Position(keyPoint.getPosition().getX() - rootPoint.getPosition().getX(), keyPoint.getPosition().getY() - rootPoint.getPosition().getY());
                vector = new KeyPointVector(position, keyPoint.getPart());
                return vector;
            case "rightAnkle":
                rootPoint = getRootPoint(keyPointList, "rightKnee");
                position = new Position(keyPoint.getPosition().getX() - rootPoint.getPosition().getX(), keyPoint.getPosition().getY() - rootPoint.getPosition().getY());
                vector = new KeyPointVector(position, keyPoint.getPart());
                return vector;
        }
        return null;
    }
}
