package com.group9.pdst.handler;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.group9.pdst.model.*;
import com.group9.pdst.utils.CalculationUtilities;
import com.group9.pdst.utils.ConstantUtilities;
import com.group9.pdst.utils.OpenBrowserUtilities;
import com.group9.pdst.utils.TranslationUtilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PoseMatchingHandler {
    //allowable deviation range
    private double standardDeviation = CalculationUtilities.calculateStandardDeviation(0, ConstantUtilities.imgSize);

    public List<SuggestionDetail> makeSuggestionDetails(List<String> simgList, List<String> imgList, String suggestionId) {
        List<SuggestionDetail> finalResult = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        String matchingResultJSON;
        for (int i = 0; i < simgList.size(); i++) {
            String simgName = simgList.get(i);
            MatchingPoseResult finalPoseResult = null;
            double maxPercent = 0;
            for (int j = 0; j < imgList.size(); j++) {
                String uri = "http://localhost:8080/poseDetect.html";
                String imgName = imgList.get(j);
                uri+="?img=" + imgName + "&simg=" + simgName + "&imgSize=" + ConstantUtilities.imgSize + "&suggestionId=" + suggestionId;
                OpenBrowserUtilities.openBrowser(uri);

                while(true) {
                    try {
                        Thread.sleep(5000);
                        matchingResultJSON = ConstantUtilities.jedis.lpop(suggestionId);
                        if(matchingResultJSON != null) {
                            MatchingPoseResult matchingResult = mapper.readValue(matchingResultJSON, MatchingPoseResult.class);
                            if(maxPercent < matchingResult.getMatchingPercentage()) {
                                maxPercent = matchingResult.getMatchingPercentage();
                                finalPoseResult = matchingResult;
                            }
                            break;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (JsonParseException e) {
                        e.printStackTrace();
                    } catch (JsonMappingException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
//            System.out.println("Max: " + maxPercent);
            String suggestion = "Tư thế của bạn khớp " + CalculationUtilities.roundingPercentage(finalPoseResult.getMatchingPercentage())
                    + "%\n" + finalPoseResult.getDescription();
            SuggestionDetail suggestionDetail = new SuggestionDetail(finalPoseResult.getImgUrl(), finalPoseResult.getStandardImgUrl(), suggestion, suggestionId);
            finalResult.add(suggestionDetail);
        }
        return finalResult;
    }
    public String matchPose(Pose trainerPose, Pose traineePose) {
        List<KeyPoint> trainerPoints = trainerPose.getKeypoints();
        List<KeyPoint> traineePoints = traineePose.getKeypoints();
        PretreatmentHandler handler = new PretreatmentHandler();
        handler.pretreatment(trainerPoints, traineePoints);
        List<MatchingPointResult> keyPointsResult = new ArrayList<MatchingPointResult>();
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
