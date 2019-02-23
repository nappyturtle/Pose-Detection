package com.group9.pdst.utils;

import com.group9.pdst.model.MatchingPointResult;

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
            case "Torso":
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
            case "Torso":
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
}
