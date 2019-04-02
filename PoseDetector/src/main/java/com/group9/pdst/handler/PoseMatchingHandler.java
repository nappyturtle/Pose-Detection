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
            if (finalPoseResult != null) {
                if(finalPoseResult.getMaxMatchingPercentage() >= 0.7) {
                    String suggestion = "Tư thế của bạn khớp: " + CalculationUtilities.roundingPercentage(finalPoseResult.getMinMatchingPercentage())
                            + "% - " + CalculationUtilities.roundingPercentage(finalPoseResult.getMaxMatchingPercentage()) + "%\n" + finalPoseResult.getDescription();
                    SuggestionDetail suggestionDetail = new SuggestionDetail(finalPoseResult.getImgUrl(), finalPoseResult.getStandardImgUrl(), suggestion, suggestionId);
                    finalResult.add(suggestionDetail);
                    trainerFrame = i;
                }
            }
            else {
                if(ConstantUtilities.jedis.get(suggestionId + "backup_important") != null) {
                    ConstantUtilities.jedis.set(suggestionId + "_important", ConstantUtilities.jedis.get(suggestionId + "backup_important"));
                }
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

        boolean isChanged = false;
        String description = "Các bộ phận quan trọng trong tư thế này";
        String importantPoint = "";
        String importantJSON = ConstantUtilities.jedis.get(suggestionId + "_important");
        String isCompare = ConstantUtilities.jedis.get(suggestionId + "_isCompare");
        if ("true".equals(isCompare) && importantJSON != null) {
            changePoints = mapper.readValue(importantJSON, List.class);
        }
        //xu ly loai bo cac diem thua va tao diem than - torso lam trong tam
        handler.pretreatment(trainerPoints, traineePoints);
        int i = 0;
        while(i < trainerPoints.size()) {
            double partLRMatchingPercentage = 1;
            double weight = 1;
            KeyPoint trainerPoint = trainerPoints.get(i);
            KeyPoint traineePoint = traineePoints.get(i);
            if (!"torso".equals(trainerPoint.getPart())) {
                System.out.println("=============================");
                System.out.println("Part: " + traineePoint.getPart());
                KeyPointVector trainerPointVector = TransUtilities.createVectorFromKeypoint(trainerPoints, trainerPoint, "torso");
                KeyPointVector traineePointVector = TransUtilities.createVectorFromKeypoint(traineePoints, traineePoint, "torso");
                if(trainerPoint.getPart().contains("left")) {
                    //get right part
                    i++;
                    KeyPoint trainerRightPoint = trainerPoints.get(i);
                    KeyPoint traineeRightPoint = traineePoints.get(i);
                    //different of Trainer left part - right part
                    double x = trainerRightPoint.getPosition().getX() - trainerPoint.getPosition().getX();
                    double y = trainerRightPoint.getPosition().getY() - trainerPoint.getPosition().getY();
                    //vector between left part - right part of Trainer
                    KeyPointVector v1 = new KeyPointVector(new Position(x, y));

                    //different of Trainee left part - right part
                    x = traineeRightPoint.getPosition().getX() - traineePoint.getPosition().getX();
                    y = traineeRightPoint.getPosition().getY() - traineePoint.getPosition().getY();
                    //vector between left part - right part of Trainee
                    KeyPointVector v2 = new KeyPointVector(new Position(x, y));
                    
                    partLRMatchingPercentage = CalculationUtilities.calculateVectorDistanceMatchingPercentage(v1, v2);
                    System.out.println("LR Percentage: " + partLRMatchingPercentage);
                    //Matching right part between Trainer - Trainee
                    KeyPointVector trainerRightPointVector = TransUtilities.createVectorFromKeypoint(trainerPoints, trainerRightPoint, "torso");
                    KeyPointVector traineeRightPointVector = TransUtilities.createVectorFromKeypoint(traineePoints, traineeRightPoint, "torso");
                    double maxPointPercentage = CalculationUtilities.calculateCosine(trainerRightPointVector, traineeRightPointVector) ;
                    System.out.println("Right cos: " + maxPointPercentage);
                    maxPointPercentage = 1 - (Math.toDegrees(Math.acos(maxPointPercentage)) / 90);

                    System.out.println("Right: " + maxPointPercentage);
                    maxPointPercentage = maxPointPercentage * partLRMatchingPercentage;
//                    maxPointPercentage = (maxPointPercentage + partLRMatchingPercentage)/2;
                    double maxScore = Math.max(traineeRightPoint.getScore(), trainerRightPoint.getScore());
                    double minScore = Math.min(traineeRightPoint.getScore(), trainerRightPoint.getScore());
                    double confidenceRatio = minScore / maxScore;
                    System.out.println("Confidence: " + confidenceRatio);
                    double minPointPercentage = maxPointPercentage * confidenceRatio;
                    if (confidenceRatio < 0.8) {
                        maxPointPercentage = maxPointPercentage * confidenceRatio;
                        minPointPercentage = minPointPercentage * confidenceRatio;
                    }
//                    if(maxPointPercentage - minPointPercentage > 0.1) {
//                        maxPointPercentage = (maxPointPercentage + minPointPercentage)/2;
//                    }
                    MatchingPointResult point = new MatchingPointResult(trainerRightPoint.getPart(), maxPointPercentage, minPointPercentage);

                    if ("false".equals(isCompare)) {
                        changePoints.add(mapper.writeValueAsString(point));
                        if(maxPointPercentage < Math.cos(Math.toRadians(45))) {
                            isChanged = true;
                        }
                    }
                    else {
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
                //Matching left part between Trainer - Trainee
                double maxPointPercentage = CalculationUtilities.calculateCosine(trainerPointVector, traineePointVector);
                System.out.println("Left cos: " + maxPointPercentage);
                System.out.println();
                maxPointPercentage = 1 - (Math.toDegrees(Math.acos(maxPointPercentage)) / 90);
                System.out.println("Left: " + maxPointPercentage);
                maxPointPercentage = maxPointPercentage * partLRMatchingPercentage;
//                maxPointPercentage = (maxPointPercentage + partLRMatchingPercentage)/2;
                double maxScore = Math.max(traineePoint.getScore(), trainerPoint.getScore());
                double minScore = Math.min(traineePoint.getScore(), trainerPoint.getScore());
                double confidenceRatio = minScore / maxScore;
                System.out.println("Confidence: " + confidenceRatio);
                double minPointPercentage = maxPointPercentage * confidenceRatio;
                //
                if (confidenceRatio < 0.8) {
                    maxPointPercentage = maxPointPercentage * confidenceRatio;
                    minPointPercentage = minPointPercentage * confidenceRatio;
                }
//                if(maxPointPercentage - minPointPercentage > 0.1) {
//                    maxPointPercentage = (maxPointPercentage + minPointPercentage)/2;
//                }
                //
                MatchingPointResult point = new MatchingPointResult(trainerPoint.getPart(), maxPointPercentage, minPointPercentage);
                if ("false".equals(isCompare)) {
                    changePoints.add(mapper.writeValueAsString(point));
                    if(maxPointPercentage < Math.cos(Math.toRadians(45))) {
                        isChanged = true;
                    }
                }
                else {
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
            i++;
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
            if(importantJSON != null) {
                ConstantUtilities.jedis.set(suggestionId + "backup_important", importantJSON);
            }
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
        if (importantPoint.getMaxMatchingPercentage() < 0.95 && confidenceRatio > 0.5) {
            if (point.getMinMatchingPercentage() >= 0.4) {
                return TransUtilities.transPartToVietnamese(point.getPart()) + ": khớp " + CalculationUtilities.roundingPercentage(point.getMinMatchingPercentage()) + "% - "
                        + CalculationUtilities.roundingPercentage(point.getMaxMatchingPercentage()) + "%\n";
            } else {
                return TransUtilities.transPartToVietnamese(point.getPart()) + " không khớp\n";
            }
        }
        return "";
    }

}
