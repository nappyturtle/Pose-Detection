package com.group9.pdst.handler;

import com.group9.pdst.model.KeyPoint;
import com.group9.pdst.model.Position;

import java.util.List;

public class PretreatmentHandler {
    public void pretreatment(List<KeyPoint> trainerPoints, List<KeyPoint> traineePoints) {
        removeExcessPoints(trainerPoints);
        removeExcessPoints(traineePoints);
//        int count = 0;
//        do {
//            KeyPoint trainerKeyPoint = trainerPoints.get(count);
//            KeyPoint traineeKeyPoint = traineePoints.get(count);
//            if (Math.abs(trainerKeyPoint.getScore() - traineeKeyPoint.getScore()) > 0.2) {
//                traineePoints.remove(traineeKeyPoint);
//                trainerPoints.remove(trainerKeyPoint);
//            }
//            else {
//                count++;
//            }
//        }
//        while(count < trainerPoints.size());
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
