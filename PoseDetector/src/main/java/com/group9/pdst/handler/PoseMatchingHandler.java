package com.group9.pdst.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.group9.pdst.model.*;
import com.group9.pdst.utils.CalculationUtilities;
import com.group9.pdst.utils.ConstantUtilities;
import com.group9.pdst.utils.TranslationUtilities;

import java.util.ArrayList;
import java.util.List;

public class PoseMatchingHandler {
    //allowable deviation range
    private double standardDeviation = CalculationUtilities.calculateStandardDeviation(0, ConstantUtilities.imgSize);

    public String matchPose(Pose trainerPose, Pose traineePose) {
        List<KeyPoint> trainerPoints = trainerPose.getKeypoints();
        List<KeyPoint> traineePoints = traineePose.getKeypoints();

        PretreatmentHandler handler = new PretreatmentHandler();
        handler.pretreatment(trainerPoints, traineePoints);
//        MatchingPoseResult poseResult = new MatchingPoseResult();
        List<MatchingPointResult> keyPointsResult = new ArrayList<MatchingPointResult>();
//        String result = "";
        double posePercentage = 0;
        for (int i = 0; i < trainerPoints.size(); i++) {
            KeyPoint trainerPoint = trainerPoints.get(i);
            KeyPoint traineePoint = traineePoints.get(i);
            //Calculate distance between 2 corresponding points in 2 images
            double xDeviation = trainerPoint.getPosition().getX() - traineePoint.getPosition().getX();
            double yDeviation = trainerPoint.getPosition().getY() - traineePoint.getPosition().getY();
            //combine distance and confident score to decrease effect of not confident points
            //divided by standard deviation to get deviation percentage
            double pointDeviationPercentage = CalculationUtilities.calculateEuclideanDistance(xDeviation, yDeviation) * trainerPoint.getScore() / standardDeviation;
            double pointPercentage = 0;
            if (pointDeviationPercentage <= 1) {
                pointPercentage = 1 - pointDeviationPercentage;
            }
            MatchingPointResult point = new MatchingPointResult(traineePoint.getPart(), xDeviation, yDeviation, pointPercentage);
            keyPointsResult.add(point);
            posePercentage += pointPercentage;
        }
        posePercentage = posePercentage / keyPointsResult.size();

        String description = "Bạn cần lưu ý các bộ phận:\n";
        description += getNoteworthyPoints(keyPointsResult, posePercentage);

        MatchingPoseResult matchingPoseResult = new MatchingPoseResult(traineePose.getUrl(), trainerPose.getUrl(), description, posePercentage);
//        poseResult.setMatchingPercentage(posePercentage);
//        result += "Bạn đã tập khớp: " + CalculationUtilities.roundingPercentage(posePercentage) + "%\nBạn cần lưu ý các bộ phận:\n";
//        result += getNoteworthyPoints(keyPointsResult, posePercentage);
//        SuggestionDetail suggestionDetail = new SuggestionDetail( traineePose.getUrl(), trainerPose.getUrl(), description, posePercentage);
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(matchingPoseResult);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }

    //Get points which have matching percentage lower pose matching percentage
    private String getNoteworthyPoints(List<MatchingPointResult> keyPoints, double posePercentage) {
        String result = "";
        for (int i = 0; i < keyPoints.size(); i++) {
            MatchingPointResult matchingPointResult = keyPoints.get(i);
            if (matchingPointResult.getMatchingPercentage() < posePercentage) {
                result += TranslationUtilities.transPartToVietnamese(matchingPointResult.getPart()) + ": khớp " + CalculationUtilities.roundingPercentage(matchingPointResult.getMatchingPercentage()) + "%\n";
            }
        }
        return result;
    }
}
