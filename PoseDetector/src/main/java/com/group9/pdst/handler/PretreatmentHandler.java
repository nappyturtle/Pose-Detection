package com.group9.pdst.handler;

import com.group9.pdst.model.KeyPoint;
import com.group9.pdst.model.KeyPointVector;
import com.group9.pdst.model.Position;
import com.group9.pdst.utils.CalculationUtilities;
import com.group9.pdst.utils.TransUtilities;

import java.util.List;

public class PretreatmentHandler {
    private double ankleCosine;

    public double getAnkleCosine() {
        return ankleCosine;
    }

    public void setAnkleCosine(double ankleCosine) {
        this.ankleCosine = ankleCosine;
    }
    public KeyPointVector getPairPartVector(List<KeyPoint> keypoints, String part) {
        KeyPoint leftPart = null;
        for(int i=0; i<keypoints.size(); i++) {
            KeyPoint keyPoint = keypoints.get(i);
            if(keyPoint.getPart().equals("left" + part)) {
                leftPart = keyPoint;
                break;
            }
        }
        KeyPointVector v = TransUtilities.createVectorFromKeypoint(keypoints, leftPart, "right" + part);
        return v;
    }
    public void pretreatment(List<KeyPoint> trainerPoints, List<KeyPoint> traineePoints) {
        removeExcessPoints(trainerPoints);
        removeExcessPoints(traineePoints);
//        KeyPointVector v1 = getPairPartVector(trainerPoints, "Ankle");
//        KeyPointVector v2 = getPairPartVector(traineePoints, "Ankle");
//        ankleCosine = CalculationUtilities.calculateCosine(v1,v2);
    }
    //remove unnecessary points
    private void removeExcessPoints(List<KeyPoint> keypoints) {
        createTorsoPoint(keypoints);
        keypoints.removeIf(keyPoint -> keyPoint.getPart().contains("Ear") || keyPoint.getPart().contains("Eye") || keyPoint.getPart().contains("Hip"));
    }
    //Combine 2 Hip to create 1 Torso
    private void createTorsoPoint(List<KeyPoint> keypoints) {
        KeyPoint torso = new KeyPoint();
        boolean created = true;
        torso.setPart("torso");
        double x = 0;
        double y = 0;
        double score = 0;
        for(int i=0; i<keypoints.size(); i++) {
            KeyPoint keyPoint = keypoints.get(i);
            if(keyPoint.getPart().contains("Hip")) {
                created = false;
                x += keyPoint.getPosition().getX();
                y += keyPoint.getPosition().getY();
                score += keyPoint.getScore();
            }
        }
        if(!created) {
            torso.setScore(score / 2);
            Position position = new Position(x / 2, y / 2);
            torso.setPosition(position);
            keypoints.add(torso);
        }
    }
}
