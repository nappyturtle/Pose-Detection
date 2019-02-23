package com.group9.pdst.handler;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.group9.pdst.model.*;
import com.group9.pdst.utils.CalculationUtilities;
import com.group9.pdst.utils.ConstantUtilities;
import com.group9.pdst.utils.OpenBrowserUtilities;
import com.group9.pdst.utils.TransUtilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PoseMatchingHandler {
    //allowable deviation range
    private double standardDeviation = CalculationUtilities.calculateStandardDeviation(0, ConstantUtilities.imgSize);

    public List<SuggestionDetail> makeSuggestionDetails(List<String> simgList, List<String> imgList, String suggestionId, String trainerFolder, String traineeFolder) {
        List<SuggestionDetail> finalResult = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        String matchingResultJSON;
        for (int i = 0; i < simgList.size(); i++) {
            String simgName = simgList.get(i);
            MatchingPoseResult finalPoseResult = null;
            double maxPercent = 0;
            double minPercent = 0;
            for (int j = 0; j < imgList.size(); j++) {
                String uri = ConstantUtilities.domain + "poseDetect.html";
                String imgName = imgList.get(j);
                uri+="?img=" + imgName + "&simg=" + simgName
                        + "&imgSize=" + ConstantUtilities.imgSize
                        + "&suggestionId=" + suggestionId
                        + "&trainerFolder=" + trainerFolder
                        + "&traineeFolder=" + traineeFolder;
                OpenBrowserUtilities.openBrowser(uri);
                while(true) {
                    try {
                        Thread.sleep(2000);
                        matchingResultJSON = ConstantUtilities.jedis.lpop(suggestionId);
                        if(matchingResultJSON != null) {
                            if(!matchingResultJSON.isEmpty()) {
                                MatchingPoseResult matchingResult = mapper.readValue(matchingResultJSON, MatchingPoseResult.class);
                                System.out.println(matchingResult);
                                if (maxPercent < matchingResult.getMaxMatchingPercentage() && minPercent < matchingResult.getMinMatchingPercentage()) {
                                    maxPercent = matchingResult.getMaxMatchingPercentage();
                                    minPercent = matchingResult.getMinMatchingPercentage();
                                    finalPoseResult = matchingResult;
                                }
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
            if(finalPoseResult != null) {
                String suggestion = "Tư thế của bạn khớp: " + CalculationUtilities.roundingPercentage(finalPoseResult.getMinMatchingPercentage())
                        + "% - " + CalculationUtilities.roundingPercentage(finalPoseResult.getMaxMatchingPercentage()) + "%\n" + finalPoseResult.getDescription();
                SuggestionDetail suggestionDetail = new SuggestionDetail(finalPoseResult.getImgUrl(), finalPoseResult.getStandardImgUrl(), suggestion, suggestionId);
                finalResult.add(suggestionDetail);
            }
        }
        return finalResult;
    }
    public String matchPose(Pose trainerPose, Pose traineePose, String suggestionId) {
        List<KeyPoint> trainerPoints = trainerPose.getKeypoints();
        List<KeyPoint> traineePoints = traineePose.getKeypoints();
        PretreatmentHandler handler = new PretreatmentHandler();
        handler.pretreatment(trainerPoints, traineePoints);
        List<MatchingPointResult> keyPointsResult = new ArrayList<MatchingPointResult>();
        double maxPosePercentage = 0;
        double minPosePercentage = 0;
        int totalWeight = 0;
        TransUtilities.legWeight = Integer.parseInt(ConstantUtilities.jedis.get(suggestionId + "_legweight"));
        TransUtilities.bodyWeight = Integer.parseInt(ConstantUtilities.jedis.get(suggestionId + "_bodyweight"));
        TransUtilities.headWeight = Integer.parseInt(ConstantUtilities.jedis.get(suggestionId + "_headweight"));

        for (int i = 0; i < trainerPoints.size(); i++) {
            KeyPoint trainerPoint = trainerPoints.get(i);
            KeyPoint traineePoint = traineePoints.get(i);

            //Calculate distance between 2 corresponding points in 2 images
            double xDeviation = trainerPoint.getPosition().getX() - traineePoint.getPosition().getX();
            double yDeviation = trainerPoint.getPosition().getY() - traineePoint.getPosition().getY();
            //combine distance and confident score to decrease effect of not confident points
            //divided by standard deviation to get deviation percentage
//            double pointDeviationPercentage = CalculationUtilities.calculateEuclideanDistance(xDeviation, yDeviation) * trainerPoint.getScore() / standardDeviation;
            double pointDeviationPercentage = CalculationUtilities.calculateEuclideanDistance(xDeviation, yDeviation) / standardDeviation;
            double maxPointPercentage = 0;
            if (pointDeviationPercentage <= 1) {
                maxPointPercentage = 1 - pointDeviationPercentage;
            }
            double minPointPercentage = maxPointPercentage * traineePoint.getScore() * trainerPoint.getScore();
            if(maxPointPercentage - minPointPercentage < 0.2) {
                MatchingPointResult point = new MatchingPointResult(traineePoint.getPart(), xDeviation, yDeviation, maxPointPercentage, minPointPercentage);
                //
                TransUtilities.setPartWeight(point);
                totalWeight += point.getWeight();
                keyPointsResult.add(point);
                maxPosePercentage += maxPointPercentage * point.getWeight();
                minPosePercentage += minPointPercentage * point.getWeight();
            }
        }
        maxPosePercentage = maxPosePercentage / totalWeight;
        minPosePercentage = minPosePercentage / totalWeight;

        //
//        String description = "Bạn cần lưu ý các bộ phận:\n";
        String description ="";
        int mostImportantPartWeight = Math.max(Math.max(TransUtilities.bodyWeight, TransUtilities.headWeight), TransUtilities.legWeight);
        description += getNoteworthyPoints(keyPointsResult, mostImportantPartWeight, maxPosePercentage, minPosePercentage);


        MatchingPoseResult matchingPoseResult = new MatchingPoseResult(traineePose.getUrl(), trainerPose.getUrl(), description, maxPosePercentage, minPosePercentage);
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(matchingPoseResult);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }

    //Get points which have matching percentage lower pose matching percentage
    private String getNoteworthyPoints(List<MatchingPointResult> keyPoints, int importantPartWeight, double maxPosePercentage, double minPosePercentage) {
        String result = "";
        String importantParts = "Các bộ phận quan trọng trong tư thế này: \n";
        String maxMatchingParts = "";
        String minMatchingParts = "";
        for (int i = 0; i < keyPoints.size(); i++) {
            MatchingPointResult matchingPointResult = keyPoints.get(i);
//            if(matchingPointResult.getMaxMatchingPercentage() - matchingPointResult.getMinMatchingPercentage() < 0.2) {
                if (matchingPointResult.getWeight() == importantPartWeight) {
                    importantParts += TransUtilities.transPartToVietnamese(matchingPointResult.getPart()) + ": khớp " + CalculationUtilities.roundingPercentage(matchingPointResult.getMinMatchingPercentage()) + "% - "
                            + CalculationUtilities.roundingPercentage(matchingPointResult.getMaxMatchingPercentage()) + "%\n";
                }
//                else if (matchingPointResult.getMaxMatchingPercentage() > maxPosePercentage) {
//                    maxMatchingParts += TransUtilities.transPartToVietnamese(matchingPointResult.getPart()) + ": khớp " + CalculationUtilities.roundingPercentage(matchingPointResult.getMinMatchingPercentage()) + "% - "
//                            + CalculationUtilities.roundingPercentage(matchingPointResult.getMaxMatchingPercentage()) + "%\n";
//                } else if (matchingPointResult.getMinMatchingPercentage() < minPosePercentage) {
//                    minMatchingParts += TransUtilities.transPartToVietnamese(matchingPointResult.getPart()) + ": khớp " + CalculationUtilities.roundingPercentage(matchingPointResult.getMinMatchingPercentage()) + "% - "
//                            + CalculationUtilities.roundingPercentage(matchingPointResult.getMaxMatchingPercentage()) + "%\n";
//                }
            }
//        }
        result += importantParts + "Các bộ phận còn lại \nCác bộ phận có độ khớp cao: \n" + maxMatchingParts + "Các bộ phận có độ khớp thấp:\n" + minMatchingParts;
        return result;
    }
}
