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
import java.util.ArrayList;
import java.util.List;

public class PoseMatchingHandler {
    //allowable deviation range
    private double standardDeviation = CalculationUtilities.calculateStandardDeviation(0, ConstantUtilities.imgSize);

    //duyet tung frame trong 2 list va thuc hien matching, kt kq tra ve trong redis voi key la suggestionid
    //tra ve danh sach cac suggestion details
    public List<SuggestionDetail> makeSuggestionDetails(List<String> simgList, List<String> imgList, String suggestionId, String trainerFolder, String traineeFolder) throws JsonProcessingException {
        List<SuggestionDetail> finalResult = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        String matchingResultJSON;
        boolean flag = false;
        for (int i = 0; i < simgList.size(); i++) {
            String simgName = simgList.get(i);
            MatchingPoseResult finalPoseResult = null;
            double maxPercent = 0;
//            double minPercent = 0;
            //
            if (i > 0) {
                ConstantUtilities.jedis.set(suggestionId + "_isCompare", "false");
                flag = checkMatchingPrecondition(simgList.get(i - 1), simgName, trainerFolder, suggestionId);
            }

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
            }
            if (finalPoseResult != null) {
                String suggestion = "Tư thế của bạn khớp: " + CalculationUtilities.roundingPercentage(finalPoseResult.getMinMatchingPercentage())
                        + "% - " + CalculationUtilities.roundingPercentage(finalPoseResult.getMaxMatchingPercentage()) + "%\n" + finalPoseResult.getDescription();
                SuggestionDetail suggestionDetail = new SuggestionDetail(finalPoseResult.getImgUrl(), finalPoseResult.getStandardImgUrl(), suggestion, suggestionId);
                finalResult.add(suggestionDetail);
            }
        }
        return finalResult;
    }

    //kiem tra xem tu the o 2 frame co thay doi hay khong
    public boolean checkMatchingPrecondition(String formerImgName, String imgName, String folderName, String suggestionId) {
        String result;
        ObjectMapper mapper = new ObjectMapper();
        String testStandardUri = ConstantUtilities.domain + "poseDetect.html";
        testStandardUri += "?img=" + formerImgName + "&simg=" + imgName
                + "&imgSize=" + ConstantUtilities.imgSize
                + "&suggestionId=" + suggestionId
                + "&trainerFolder=" + folderName
                + "&traineeFolder=" + folderName;
        OpenBrowserUtilities.openBrowser(testStandardUri);
        while (true) {
            try {
                Thread.sleep(2000);
                result = ConstantUtilities.jedis.lpop(suggestionId);
                if (result != null) {
                    MatchingPoseResult matchingResult = mapper.readValue(result, MatchingPoseResult.class);
                    if (matchingResult.getMaxMatchingPercentage() > 0.9) {
                        return false; // lon hon 90% thi khong can so sanh nua
                    }
                    return true;
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
        PretreatmentHandler handler = new PretreatmentHandler();
        List<MatchingPointResult> keyPointsResult = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        List<MatchingPointResult> importantPoints = new ArrayList<>();
        List<String> changePoints = new ArrayList<>();
        double maxPosePercentage = 0;
        double minPosePercentage = 0;
        int totalWeight = 0;
        TransUtilities.legWeight = Integer.parseInt(ConstantUtilities.jedis.get(suggestionId + "_legweight"));
        TransUtilities.bodyWeight = Integer.parseInt(ConstantUtilities.jedis.get(suggestionId + "_bodyweight"));
        TransUtilities.headWeight = Integer.parseInt(ConstantUtilities.jedis.get(suggestionId + "_headweight"));
        String description = "";
        String importantJSON = ConstantUtilities.jedis.get(suggestionId + "_important");
        String isCompare = ConstantUtilities.jedis.get(suggestionId + "_isCompare");

        handler.pretreatment(trainerPoints, traineePoints);
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
            double minPointPercentage = maxPointPercentage * Math.max(traineePoint.getScore(), trainerPoint.getScore());

            //do lech giua max va min cang nho thi do tin cay cang cao, o day yeu cau do tin cay tren 80%
            MatchingPointResult point = new MatchingPointResult(traineePoint.getPart(), xDeviation, yDeviation, maxPointPercentage, minPointPercentage);
            TransUtilities.setPartWeight(point);
            totalWeight += point.getWeight();
            keyPointsResult.add(point);
            maxPosePercentage += maxPointPercentage * point.getWeight();
            minPosePercentage += minPointPercentage * point.getWeight();
            //cac diem lech nhau tren 80% la cac diem chuyen dong so voi frame truoc
            if ("false".equals(isCompare) && maxPointPercentage < 0.8) {
                changePoints.add(point.getPart());
            }

        } //het dong for

        if("false".equals(isCompare)) {
            ConstantUtilities.jedis.set(suggestionId + "_important", mapper.writeValueAsString(changePoints));
        }
        else {
            if(importantJSON != null) {
                changePoints = mapper.readValue(importantJSON, ArrayList.class);
                for (MatchingPointResult matchingPointResult: keyPointsResult) {
                    if(changePoints.contains(matchingPointResult.getPart())) {
                       importantPoints.add(matchingPointResult);
                    }
                }
            }
            //Frame dau tien khong co frame truoc de so, su dung weight do nguoi dung cung cap
            else {
//                int highestWeight = Math.max(Math.max(TransUtilities.bodyWeight, TransUtilities.headWeight), TransUtilities.legWeight);
//                for (MatchingPointResult matchingPointResult: keyPointsResult) {
//                    if(matchingPointResult.getWeight() == highestWeight) {
//                        importantPoints.add(matchingPointResult);
//                    }
//                }
            }
            description += getNoteworthyPoints(importantPoints);
        }
        maxPosePercentage = maxPosePercentage / totalWeight;
        minPosePercentage = minPosePercentage / totalWeight;
        MatchingPoseResult matchingPoseResult = new MatchingPoseResult(traineePose.getUrl(), trainerPose.getUrl(), description, maxPosePercentage, minPosePercentage);
        System.out.println(matchingPoseResult + "\n========================\n");
        return matchingPoseResult;
    }

    //tra ve description cac diem quan trong cua dong tac
    private String getNoteworthyPoints(List<MatchingPointResult> importantPoints) {
        String importantParts = "Các bộ phận quan trọng trong tư thế này: \n";
        for (int i = 0; i < importantPoints.size(); i++) {
            MatchingPointResult importantPoint = importantPoints.get(i);
            importantParts += TransUtilities.transPartToVietnamese(importantPoint.getPart()) + ": khớp " + CalculationUtilities.roundingPercentage(importantPoint.getMinMatchingPercentage()) + "% - "
                    + CalculationUtilities.roundingPercentage(importantPoint.getMaxMatchingPercentage()) + "%\n";
        }
        return importantParts;
    }
}
