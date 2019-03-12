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
import org.jcodec.containers.mp4.SampleOffsetUtils;

import java.io.IOException;
import java.util.*;

public class PoseMatchingHandler {
    //allowable deviation range
    private double standardDeviation = CalculationUtilities.calculateStandardDeviation(0, ConstantUtilities.imgSize);


    //duyet tung frame trong 2 list va thuc hien matching, kt kq tra ve trong redis voi key la suggestionid
    //tra ve danh sach cac suggestion details
    public List<SuggestionDetail> makeSuggestionDetails(List<String> simgList, List<String> imgList, String suggestionId, String trainerFolder, String traineeFolder) throws JsonProcessingException {
        List<SuggestionDetail> finalResult = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        String matchingResultJSON;
        int trainerFrame = 0;
//        int traineeFrame = 0;
        for (int i = 1; i < simgList.size(); i++) {
            String simgName = simgList.get(i);
            MatchingPoseResult finalPoseResult = null;
            double maxPercent = 0;
//            double minPercent = 0;
            //
            ConstantUtilities.jedis.set(suggestionId + "_isCompare", "false");
            boolean flag = checkMatchingPrecondition(simgList.get(trainerFrame), simgName, trainerFolder, suggestionId);
            //
            if (flag) {

                ConstantUtilities.jedis.set(suggestionId + "_isCompare", "true");
                for (int j = 0; j < imgList.size(); j++) {
                    String uri = ConstantUtilities.domain + "poseDetect.html";
                    String imgName = imgList.get(j);
                    uri += "?img=" + imgName + "&simg=" + simgName
                            + "&imgSize=" + ConstantUtilities.imgSize
                            + "&suggestionId=" + suggestionId
                            + "&trainerFolder=" + trainerFolder
                            + "&traineeFolder=" + traineeFolder;
                    OpenBrowserUtilities.openBrowser(uri);
                    while (true) {
                        try {
                            Thread.sleep(2000);
                            matchingResultJSON = ConstantUtilities.jedis.lpop(suggestionId);
                            if (matchingResultJSON != null) {
                                MatchingPoseResult matchingResult = mapper.readValue(matchingResultJSON, MatchingPoseResult.class);
                                if (maxPercent < matchingResult.getMaxMatchingPercentage()) {
                                    maxPercent = matchingResult.getMaxMatchingPercentage();
//                                    minPercent = matchingResult.getMinMatchingPercentage();
                                    finalPoseResult = matchingResult;
//                                    trainerFrame=i;
//                                    traineeFrame = j;
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

            }
            if (finalPoseResult != null && finalPoseResult.getMaxMatchingPercentage() >= 0.7) {
//            if (finalPoseResult != null && finalPoseResult.getMaxMatchingPercentage() >= 0.8) {
                String suggestion = "Tư thế của bạn khớp: " + CalculationUtilities.roundingPercentage(finalPoseResult.getMinMatchingPercentage())
                        + "% - " + CalculationUtilities.roundingPercentage(finalPoseResult.getMaxMatchingPercentage()) + "%\n" + finalPoseResult.getDescription();
                SuggestionDetail suggestionDetail = new SuggestionDetail(finalPoseResult.getImgUrl(), finalPoseResult.getStandardImgUrl(), suggestion, suggestionId);
                finalResult.add(suggestionDetail);
                trainerFrame = i;
//                imgList.remove(traineeFrame);
            }
            else {
                ConstantUtilities.jedis.set(suggestionId + "_important", ConstantUtilities.jedis.get(suggestionId + "backup_important"));
            }
        }
        return finalResult;
    }

    //kiem tra xem tu the o 2 frame co thay doi hay khong
    public boolean checkMatchingPrecondition(String formerImgName, String imgName, String folderName, String suggestionId) {
        String result;
        String testStandardUri = ConstantUtilities.domain + "poseDetect.html";
        testStandardUri += "?img=" + imgName + "&simg=" + formerImgName
                + "&imgSize=" + ConstantUtilities.imgSize
                + "&suggestionId=" + suggestionId
                + "&trainerFolder=" + folderName
                + "&traineeFolder=" + folderName;
        OpenBrowserUtilities.openBrowser(testStandardUri);
        while (true) {
            try {
                Thread.sleep(2000);
                result = ConstantUtilities.jedis.lpop(suggestionId);
                if(result != null) {
                    boolean isChanged = Boolean.parseBoolean(ConstantUtilities.jedis.get(suggestionId + "_changed"));
                    if (isChanged) {
                        ConstantUtilities.jedis.set(suggestionId + "_changed", "false");
                        return true;
                    }
                    else {
                        return false;
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    //thuc hien matching 2 pose va luu ket qua vao redis thong qua key la suggestionid
    public MatchingPoseResult matchPose(Pose trainerPose, Pose traineePose, String suggestionId) throws IOException {
        List<KeyPoint> trainerPoints = trainerPose.getKeypoints();
        List<KeyPoint> traineePoints = traineePose.getKeypoints();
        List<MatchingPointResult> keyPointsResult = new ArrayList<>();
        List<String> changePoints = new ArrayList<>();

        PretreatmentHandler handler = new PretreatmentHandler();
        ObjectMapper mapper = new ObjectMapper();

        double maxPosePercentage = 0;
        double minPosePercentage = 0;
        double totalWeight = 0;
        Position trainerPointPosition = null;
        Position traineePointPosition = null;

        boolean isChanged = false;
        String description = "Các bộ phận quan trọng trong tư thế này";
        String importantPoint = "";
        String importantJSON = ConstantUtilities.jedis.get(suggestionId + "_important");
        String isCompare = ConstantUtilities.jedis.get(suggestionId + "_isCompare");
        if ("true".equals(isCompare) && importantJSON != null) {
            changePoints = mapper.readValue(importantJSON, List.class);
            //
            System.out.println("Changed point: ");
            for (int i = 0; i < changePoints.size(); i++) {
                String s =  changePoints.get(i);
                MatchingPointResult matchingPointResult = mapper.readValue(s, MatchingPointResult.class);
                System.out.println(matchingPointResult);
            }
            System.out.println("===========\n");
            //
        }
        //xu ly loai bo cac diem thua va tao diem than - torso lam trong tam
        handler.pretreatment(trainerPoints, traineePoints);
        for (int i = 0; i < trainerPoints.size(); i++) {
            System.out.println("\n===========AAAA==============\n");
            double distanceRatio = 1;
            double weight = 1;
            KeyPoint trainerPoint = trainerPoints.get(i);
            KeyPoint traineePoint = traineePoints.get(i);
            if (!"torso".equals(trainerPoint.getPart())) {
                KeyPointVector trainerPointVector = TransUtilities.createVectorFromKeypoint(trainerPoints, trainerPoint, "torso");
                KeyPointVector traineePointVector = TransUtilities.createVectorFromKeypoint(traineePoints, traineePoint, "torso");
                if(trainerPoint.getPart().contains("left")) {
                    trainerPointPosition = trainerPoint.getPosition();
                    traineePointPosition = traineePoint.getPosition();
                }
                if(trainerPoint.getPart().contains("right")) {
                    double trainerDistance = CalculationUtilities.calculateEuclideanDistance(trainerPointPosition.getX() - trainerPoint.getPosition().getX(), trainerPointPosition.getY() - trainerPoint.getPosition().getY());
                    double traineeDistance = CalculationUtilities.calculateEuclideanDistance(traineePointPosition.getX() - traineePoint.getPosition().getX(), traineePointPosition.getY() - traineePoint.getPosition().getY());
//                    System.out.println("Trainer " + trainerPoint.getPart() + " distance: " + trainerDistance);
//                    System.out.println("Trainee " + traineePoint.getPart() + " distance: " + traineeDistance);
                    distanceRatio = 1 - Math.abs(traineeDistance - trainerDistance)/standardDeviation;
//                    distanceRatio = trainerDistance > traineeDistance ? traineeDistance/trainerDistance : trainerDistance/traineeDistance;
                }
                System.out.println("Distance ratio: " + distanceRatio);
                double maxPointPercentage = CalculationUtilities.calculateCosine(trainerPointVector, traineePointVector) * distanceRatio;
                double minPointPercentage = maxPointPercentage * Math.min(traineePoint.getScore()/trainerPoint.getScore(), trainerPoint.getScore()/traineePoint.getScore());
                if(maxPointPercentage - minPointPercentage > 0.1) {
                    maxPointPercentage = (maxPointPercentage + minPointPercentage)/2;
                }
                if(maxPointPercentage < Math.cos(Math.toRadians(45))) {
//                    System.out.println("Change Part: " + traineePoint.getPart());
                    isChanged = true;
                }

                MatchingPointResult point = new MatchingPointResult(trainerPoint.getPart(), maxPointPercentage, minPointPercentage);

                if ("false".equals(isCompare)) {
                    changePoints.add(mapper.writeValueAsString(point));
                    System.out.println("Point: " + point.getPart());
                    System.out.println("Match: " + point.getMaxMatchingPercentage() + "\n");
                }
                else {
                    double maxScore = Math.max(traineePoint.getScore(), trainerPoint.getScore());
                    double minScore = Math.min(traineePoint.getScore(), trainerPoint.getScore());
                    double confidenceRatio = minScore / maxScore;
                    System.out.println("Point: \n" + point);
                    System.out.println("Score: " + confidenceRatio);
                        for (String changedPointJSON : changePoints) {
                            MatchingPointResult changedPoint = mapper.readValue(changedPointJSON, MatchingPointResult.class);
                            if (changedPoint.getPart().equals(point.getPart())) {
                                weight = TransUtilities.getWeight(changedPoint.getMaxMatchingPercentage());
                                importantPoint += getNoteworthyPoint(changedPoint, point, confidenceRatio);
                                break;
                            }
                        }
                }
                totalWeight += weight;
                keyPointsResult.add(point);
                maxPosePercentage += point.getMaxMatchingPercentage() * weight;
                minPosePercentage += point.getMinMatchingPercentage() * weight;

            }
        }


        maxPosePercentage = maxPosePercentage / totalWeight;
        minPosePercentage = minPosePercentage / totalWeight;
        if(importantPoint.isEmpty()) {
            description += " không thể nhận diện được trong tư thế của bạn";
        }
        else {
            description += ":\n" + importantPoint;
        }
        MatchingPoseResult matchingPoseResult = new MatchingPoseResult(traineePose.getUrl(), trainerPose.getUrl(), description, maxPosePercentage, minPosePercentage);

        System.out.println(matchingPoseResult + "\n========================\n");
        if ("false".equals(isCompare)) {
            ConstantUtilities.jedis.set(suggestionId + "_important", mapper.writeValueAsString(changePoints));
            ConstantUtilities.jedis.set(suggestionId + "backup_important", importantJSON);
            if(isChanged) {
                ConstantUtilities.jedis.set(suggestionId + "_changed", "true");
            }
            else {
                ConstantUtilities.jedis.set(suggestionId + "_changed", "false");
            }
        }
        return matchingPoseResult;
    }

    //tra ve description cac diem quan trong cua dong tac
    private String getNoteworthyPoint(MatchingPointResult importantPoint, MatchingPointResult point, double confidenceRatio) {
        if (importantPoint.getMaxMatchingPercentage() < Math.cos(Math.toRadians(15))) {
            if (confidenceRatio < 0.8) {
                point.setMaxMatchingPercentage(point.getMaxMatchingPercentage() * confidenceRatio);
                point.setMinMatchingPercentage(point.getMinMatchingPercentage() * confidenceRatio);
            }
            if (point.getMinMatchingPercentage() >= Math.cos(Math.toRadians(60))) {
                return TransUtilities.transPartToVietnamese(point.getPart()) + ": khớp " + CalculationUtilities.roundingPercentage(point.getMinMatchingPercentage()) + "% - "
                        + CalculationUtilities.roundingPercentage(point.getMaxMatchingPercentage()) + "%\n";
            } else {
                return TransUtilities.transPartToVietnamese(point.getPart()) + " không khớp\n";
            }
        }
        return "";
    }

}
