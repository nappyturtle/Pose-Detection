package com.group9.pdst.utils;

import com.group9.pdst.model.KeyPointVector;

public class CalculationUtilities {
    public static double calculateEuclideanDistance(double xDeviation, double yDeviation) {
        return Math.sqrt(Math.pow(xDeviation, 2) + Math.pow(yDeviation, 2));
    }
    public static double calculateCosine(KeyPointVector a, KeyPointVector b) {
        double standardDeviation = calculateStandardDeviation(0, ConstantUtilities.imgSize);
        double scalar = a.getPosition().getX() * b.getPosition().getX() + a.getPosition().getY() * b.getPosition().getY();
        double length1 = calculateEuclideanDistance(a.getPosition().getX(), a.getPosition().getY());
        double length2 = calculateEuclideanDistance(b.getPosition().getX(), b.getPosition().getY());

        if(scalar < 0) scalar = 0;
        scalar = scalar/(length1 * length2);
        scalar = (scalar + (1 - Math.abs(length1 - length2)/standardDeviation))/2;
//        System.out.println("Cos cua goc: " + scalar/(length1 * length2));
        return scalar;
    }
    public static double calculateStandardDeviation(double a, double b) {
        return Math.sqrt((Math.pow(b - a + 1, 2) - 1) / 12);
    }

    public static double roundingPercentage(double a) {
        return Math.round(a * 10000) * 1.0 / 100;
    }

}
