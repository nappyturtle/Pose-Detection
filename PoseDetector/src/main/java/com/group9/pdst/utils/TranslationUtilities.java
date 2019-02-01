package com.group9.pdst.utils;

public class TranslationUtilities {
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
}
