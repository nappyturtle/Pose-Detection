package com.group9.pdst.utils;

import com.group9.pdst.model.KeyPointVector;

public class CalculationUtilities {
    public static double calculateEuclideanDistance(double xDeviation, double yDeviation) {
        return Math.sqrt(Math.pow(xDeviation, 2) + Math.pow(yDeviation, 2));
    }
    public static double calculateVectorDistanceMatchingPercentage(KeyPointVector a, KeyPointVector b) {
        double standardDeviation = calculateStandardDeviation(0, ConstantUtilities.imgSize);
        double length1 = calculateEuclideanDistance(a.getPosition().getX(), a.getPosition().getY());
        double length2 = calculateEuclideanDistance(b.getPosition().getX(), b.getPosition().getY());
//        if(length1 < Math.sqrt(2) || length2 < Math.sqrt(2)) {
//            length1 = length2;
//        }
        double percentage = (1 - Math.abs(length1 - length2)/standardDeviation);
        if(percentage < 0) return 0;
        return percentage;
    }
    public static double calculateCosine(KeyPointVector a, KeyPointVector b) {
        double scalar = a.getPosition().getX() * b.getPosition().getX() + a.getPosition().getY() * b.getPosition().getY();
        double length1 = calculateEuclideanDistance(a.getPosition().getX(), a.getPosition().getY());
        double length2 = calculateEuclideanDistance(b.getPosition().getX(), b.getPosition().getY());
        if(scalar < 0) return 0;
        if(length1 < Math.sqrt(2) || length2 < Math.sqrt(2)) return 1;
        scalar = scalar/(length1 * length2);
        if(scalar > 1) return 1;
        return scalar;
    }
    public static double calculateStandardDeviation(double a, double b) {
        return Math.sqrt((Math.pow(b - a + 1, 2) - 1) / 12);
    }

    public static double roundingPercentage(double a) {
        return Math.round(a * 10000) * 1.0 / 100;
    }

}
