package com.group9.pdst.utils;

import com.group9.pdst.model.KeyPoint;
import com.group9.pdst.model.KeyPointVector;
import com.group9.pdst.model.MatchingPointResult;
import com.group9.pdst.model.Position;

import java.util.List;

public class TransUtilities {

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
                return "Cánh tay trái";
            case "rightWrist":
                return "Cánh tay phải";
            case "torso":
                return "Thân";
            case "leftKnee":
                return "Đầu gối trái";
            case "rightKnee":
                return "Đầu gối phải";
            case "leftAnkle":
                return "Chân trái";
            case "rightAnkle":
                return "Chân phải";
        }
        return "";
    }
    public static int getWeight (double maxPercentage) {
        if(maxPercentage > 0) {
            return (int)Math.round((1-maxPercentage)*9+1);
        }
        return 10;
    }
    private static KeyPoint getRootPoint(List<KeyPoint> keyPointList, String rootPointPart) {
        for (int i = 0; i < keyPointList.size(); i++) {
            KeyPoint rootPoint =  keyPointList.get(i);
            if(rootPoint.getPart().equals(rootPointPart)) return rootPoint;
        }
        return null;
    }
    public static KeyPointVector createVectorFromKeypoint(List<KeyPoint> keyPointList, KeyPoint keyPoint, String root) {
        KeyPoint rootPoint;
        Position position;
        KeyPointVector vector;
        rootPoint = getRootPoint(keyPointList, root);
        position = new Position(keyPoint.getPosition().getX() - rootPoint.getPosition().getX(), keyPoint.getPosition().getY() - rootPoint.getPosition().getY());
        vector = new KeyPointVector(position, keyPoint.getPart());
        return vector;
    }
}
