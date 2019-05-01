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

    public List<Frame> createDataset(List<String>imgList, String videoId, String trainerFolder) {
        List<Frame> dataset = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        String frameResultJSON;
        int i = 0;
        int j = 1;
        while(j < imgList.size()) {
            String simgName = imgList.get(i);
            String uri = ConstantUtilities.domain + "poseDetectForDataset.html";
            String imgName = imgList.get(j);
            uri += "?img=" + imgName + "&simg=" + simgName
                    + "&imgSize=" + ConstantUtilities.imgSize
                    + "&videoId=" + videoId
                    + "&trainerFolder=" + trainerFolder;
            OpenBrowserUtilities.openBrowser(uri);
            while (true) {
                try {
                    Thread.sleep(2000);
                    frameResultJSON = ConstantUtilities.jedis.lpop("video_" + videoId);
                    if (frameResultJSON != null) {
                        if(!frameResultJSON.equals("none")) {
                            Frame frame = mapper.readValue(frameResultJSON, Frame.class);
                            dataset.add(frame);
                            i=j;
                            j++;
                        }
                        else {
                            j++;
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
        return dataset;
    }

    //duyet tung frame trong 2 list va thuc hien matching, kt kq tra ve trong redis voi key la suggestionid
    //tra ve danh sach cac suggestion details
    public List<SuggestionDetail> makeSuggestionDetails(List<Frame> dataset, List<String> imgList, String suggestionId, String traineeFolder) throws JsonProcessingException {
        List<SuggestionDetail> finalResult = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        String matchingResultJSON;

        for (int i = 1; i < dataset.size(); i++) {
            Frame datasetFrame = dataset.get(i);
            MatchingPoseResult finalPoseResult = null;
            double maxPercent = 0;

            //
            ConstantUtilities.jedis.set(suggestionId + "_weight", mapper.writeValueAsString(datasetFrame));
//            boolean flag = checkMatchingPrecondition(simgList.get(trainerFrame), simgName, trainerFolder, suggestionId);
//            if (flag) {
//                ConstantUtilities.jedis.set(suggestionId + "_isCompare", "true");
                for (int j = 0; j < imgList.size(); j++) {
                    String uri = ConstantUtilities.domain + "poseDetect.html";
                    String imgName = imgList.get(j);
                    uri += "?img=" + imgName + "&simgUrl=" + datasetFrame.getUrl()
                            + "&imgSize=" + ConstantUtilities.imgSize
                            + "&suggestionId=" + suggestionId
                            + "&traineeFolder=" + traineeFolder;
                    OpenBrowserUtilities.openBrowser(uri);
                    while (true) {
                        try {
                            Thread.sleep(2000);
                            matchingResultJSON = ConstantUtilities.jedis.lpop("suggestion_" + suggestionId);
                            if (matchingResultJSON != null) {
                                MatchingPoseResult matchingResult = mapper.readValue(matchingResultJSON, MatchingPoseResult.class);
                                if (maxPercent < matchingResult.getMaxMatchingPercentage()) {
                                    maxPercent = matchingResult.getMaxMatchingPercentage();

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

            if (finalPoseResult != null) {
                if(finalPoseResult.getMaxMatchingPercentage() >= 0.6) {
                    String suggestion = "Tư thế của bạn khớp: " + CalculationUtilities.roundingPercentage(finalPoseResult.getMinMatchingPercentage())
                            + "% - " + CalculationUtilities.roundingPercentage(finalPoseResult.getMaxMatchingPercentage()) + "%\n" + finalPoseResult.getDescription();
                    SuggestionDetail suggestionDetail = new SuggestionDetail(finalPoseResult.getImgUrl(), finalPoseResult.getStandardImgUrl(), suggestion, suggestionId);
                    finalResult.add(suggestionDetail);
                }
            }
        }
        return finalResult;
    }

    public Frame matchPoseForDataset(Pose previousPose, Pose currentPose, String id) throws IOException {
        List<KeyPoint> currentPoints = currentPose.getKeypoints();
        List<KeyPoint> previousPoints = previousPose.getKeypoints();
        Frame currentFrame = new Frame();
        currentFrame.setVideoId(Integer.parseInt(id));
        currentFrame.setUrl(currentPose.getUrl());
        PretreatmentHandler handler = new PretreatmentHandler();

        double maxPosePercentage = 0;

        handler.pretreatment(currentPoints, previousPoints);
        int i = 0;
        while(i < currentPoints.size()) {
            double partLRMatchingPercentage = 1;
            KeyPoint currentPoint = currentPoints.get(i);
            KeyPoint previousPoint = previousPoints.get(i);
            if (!"torso".equals(currentPoint.getPart())) {
                System.out.println("=============================");
                System.out.println("Part: " + previousPoint.getPart());
                KeyPointVector currentPointVector = TransUtilities.createVectorFromKeypoint(currentPoints, currentPoint, "torso");
                KeyPointVector previousPointVector = TransUtilities.createVectorFromKeypoint(previousPoints, previousPoint, "torso");
                if(currentPoint.getPart().contains("left")) {
                    //get right part
                    i++;
                    KeyPoint currentRightPoint = currentPoints.get(i);
                    KeyPoint previousRightPoint = previousPoints.get(i);
                    //different of Trainer left part - right part
                    double x = currentRightPoint.getPosition().getX() - previousRightPoint.getPosition().getX();
                    double y = currentRightPoint.getPosition().getY() - previousRightPoint.getPosition().getY();
                    //vector between left part - right part of Trainer
                    KeyPointVector v1 = new KeyPointVector(new Position(x, y));

                    //different of Trainee left part - right part
                    x = previousRightPoint.getPosition().getX() - previousPoint.getPosition().getX();
                    y = previousRightPoint.getPosition().getY() - previousPoint.getPosition().getY();
                    //vector between left part - right part of Trainee
                    KeyPointVector v2 = new KeyPointVector(new Position(x, y));

                    partLRMatchingPercentage = CalculationUtilities.calculateVectorDistanceMatchingPercentage(v1, v2);
                    System.out.println("LR Percentage: " + partLRMatchingPercentage);
                    //Matching right part between Trainer - Trainee
                    KeyPointVector currentRightPointVector = TransUtilities.createVectorFromKeypoint(currentPoints, currentRightPoint, "torso");
                    KeyPointVector previousRightPointVector = TransUtilities.createVectorFromKeypoint(previousPoints, previousRightPoint, "torso");
                    double maxPointPercentage = CalculationUtilities.calculateCosine(currentRightPointVector, previousRightPointVector) ;
                    System.out.println("Right cos: " + maxPointPercentage);
                    maxPointPercentage = 1 - (Math.toDegrees(Math.acos(maxPointPercentage)) / 90);

                    System.out.println("Right: " + maxPointPercentage);
//                    maxPointPercentage = maxPointPercentage + partLRMatchingPercentage;
                    maxPointPercentage = (maxPointPercentage + partLRMatchingPercentage)/2;
                    double maxScore = Math.max(previousRightPoint.getScore(), currentRightPoint.getScore());
                    double minScore = Math.min(previousRightPoint.getScore(), currentRightPoint.getScore());
                    double confidenceRatio = minScore / maxScore;
                    System.out.println("Confidence: " + confidenceRatio);
                    double minPointPercentage = maxPointPercentage * confidenceRatio;
                    if(maxPointPercentage - minPointPercentage > 0.1) {
                        maxPointPercentage = (maxPointPercentage + minPointPercentage)/2;
                    }
                    if (confidenceRatio < 0.8 && confidenceRatio > 0.5) {
                        maxPointPercentage = maxPointPercentage * confidenceRatio;
                        minPointPercentage = minPointPercentage * confidenceRatio;
                    }
                    MatchingPointResult point = new MatchingPointResult(currentRightPoint.getPart(), maxPointPercentage, minPointPercentage);
                    int weight = TransUtilities.getWeight(point.getMaxMatchingPercentage());
                    System.out.println("Part: " + point.getPart() + " - Weight: " + weight);
                    currentFrame.assignWeight(point.getPart(), weight);
                    maxPosePercentage += point.getMaxMatchingPercentage();

                }
                //Matching left part between Trainer - Trainee
                double maxPointPercentage = CalculationUtilities.calculateCosine(currentPointVector, previousPointVector);
                System.out.println("Left cos: " + maxPointPercentage);
                System.out.println();
                maxPointPercentage = 1 - (Math.toDegrees(Math.acos(maxPointPercentage)) / 90);
                System.out.println("Left: " + maxPointPercentage);
                maxPointPercentage = (maxPointPercentage + partLRMatchingPercentage)/2;
                double maxScore = Math.max(previousPoint.getScore(), currentPoint.getScore());
                double minScore = Math.min(previousPoint.getScore(), currentPoint.getScore());
                double confidenceRatio = minScore / maxScore;
                System.out.println("Confidence: " + confidenceRatio);
                double minPointPercentage = maxPointPercentage * confidenceRatio;
                //
                if(maxPointPercentage - minPointPercentage > 0.1) {
                    maxPointPercentage = (maxPointPercentage + minPointPercentage)/2;
                }
                if (confidenceRatio < 0.8 && confidenceRatio > 0.5) {
                    maxPointPercentage = maxPointPercentage * confidenceRatio;
                    minPointPercentage = minPointPercentage * confidenceRatio;
                }

                MatchingPointResult point = new MatchingPointResult(currentPoint.getPart(), maxPointPercentage, minPointPercentage);
                int weight = TransUtilities.getWeight(point.getMaxMatchingPercentage());
                System.out.println("Part: " + point.getPart() + " - Weight: " + weight);
                currentFrame.assignWeight(point.getPart(), weight);
                maxPosePercentage += point.getMaxMatchingPercentage();
            }
            i++;
        }
        maxPosePercentage = maxPosePercentage / currentPoints.size();

        if(maxPosePercentage < 0.75) return currentFrame;
        else return null;
    }

    //thuc hien matching 2 pose va luu ket qua vao redis thong qua key la suggestionid
    public MatchingPoseResult matchPose(Pose trainerPose, Pose traineePose, String id) throws IOException {
        List<KeyPoint> trainerPoints = trainerPose.getKeypoints();
        List<KeyPoint> traineePoints = traineePose.getKeypoints();
        List<MatchingPointResult> keyPointsResult = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        String datasetFrameJSON = ConstantUtilities.jedis.get(id + "_weight");
        Frame datasetFrame = mapper.readValue(datasetFrameJSON, Frame.class);
        PretreatmentHandler handler = new PretreatmentHandler();

        double maxPosePercentage = 0;
        double minPosePercentage = 0;
        double totalWeight = datasetFrame.calculateTotalWeight();

//        boolean isChanged = false;
        String description = "Các bộ phận quan trọng trong tư thế này";
        String importantPoint = "";
//        String importantJSON = ConstantUtilities.jedis.get(id + "_important");
//        String isCompare = ConstantUtilities.jedis.get(suggestionId + "_isCompare");
//        if ("true".equals(isCompare) && importantJSON != null) {
//            changePoints = mapper.readValue(importantJSON, List.class);
//        }
        //xu ly loai bo cac diem thua va tao diem than - torso lam trong tam
        handler.pretreatment(trainerPoints, traineePoints);
        int i = 0;
        while(i < trainerPoints.size()) {
            double partLRMatchingPercentage = 1;
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
//                    maxPointPercentage = maxPointPercentage * partLRMatchingPercentage;
                    maxPointPercentage = (maxPointPercentage + partLRMatchingPercentage)/2;
                    double maxScore = Math.max(traineeRightPoint.getScore(), trainerRightPoint.getScore());
                    double minScore = Math.min(traineeRightPoint.getScore(), trainerRightPoint.getScore());

                    double confidenceRatio = minScore / maxScore;
                    System.out.println("Confidence: " + confidenceRatio);
                    double minPointPercentage = maxPointPercentage * confidenceRatio;
                    if(maxPointPercentage - minPointPercentage > 0.1) {
                        maxPointPercentage = (maxPointPercentage + minPointPercentage)/2;
                    }
                    if (confidenceRatio < 0.8 && confidenceRatio > 0.5) {
                        maxPointPercentage = maxPointPercentage * confidenceRatio;
                        minPointPercentage = minPointPercentage * confidenceRatio;
                    }
                    MatchingPointResult point = new MatchingPointResult(trainerRightPoint.getPart(), maxPointPercentage, minPointPercentage);

                    keyPointsResult.add(point);
                    int weight = datasetFrame.returnWeight(point.getPart());
                    importantPoint += getNoteworthyPoint(weight, point, confidenceRatio);
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
                if(maxPointPercentage - minPointPercentage > 0.1) {
                    maxPointPercentage = (maxPointPercentage + minPointPercentage)/2;
                }
                if (confidenceRatio < 0.8 && confidenceRatio > 0.5) {
                    maxPointPercentage = maxPointPercentage * confidenceRatio;
                    minPointPercentage = minPointPercentage * confidenceRatio;
                }
//                if(maxPointPercentage - minPointPercentage > 0.1) {
//                    maxPointPercentage = (maxPointPercentage + minPointPercentage)/2;
//                }
                //
                MatchingPointResult point = new MatchingPointResult(trainerPoint.getPart(), maxPointPercentage, minPointPercentage);
                keyPointsResult.add(point);
                int weight = datasetFrame.returnWeight(point.getPart());
                importantPoint += getNoteworthyPoint(weight, point, confidenceRatio);
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
        return matchingPoseResult;
    }

    //tra ve description cac diem quan trong cua dong tac
    private String getNoteworthyPoint(int weight, MatchingPointResult point, double confidenceRatio) {
        if (weight >= 4  && confidenceRatio > 0.5) {
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
