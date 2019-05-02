package com.group9.pdst.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group9.pdst.handler.PoseMatchingHandler;
import com.group9.pdst.model.Frame;
import com.group9.pdst.model.MatchingPoseResult;
import com.group9.pdst.model.Pose;
import com.group9.pdst.model.SuggestionDetail;
import com.group9.pdst.utils.ConstantUtilities;
import org.jcodec.common.model.Picture;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
public class PoseMatchingController {


    @RequestMapping(value = "/matchPose", method = RequestMethod.POST)
    public void comparePose(@RequestBody String jsonPoses) {
        MatchingPoseResult poseResult;
        Frame frameResult;
        ObjectMapper mapper = new ObjectMapper();
        List<String> poses;
        try {
            poses = mapper.readValue(jsonPoses, List.class);
                Pose trainerPose = mapper.readValue(poses.get(0), Pose.class);
                Pose traineePose = mapper.readValue(poses.get(1), Pose.class);
                String id = poses.get(2);
                String type = poses.get(3);

                PoseMatchingHandler handler = new PoseMatchingHandler();

                if("makeSuggestion".equals(type)) {
                    poseResult = handler.matchPose(trainerPose, traineePose, id);
                    ConstantUtilities.jedis.lpush("suggestion_" + id, mapper.writeValueAsString(poseResult));
                }
                else if("createDataset".equals(type)){
                    frameResult = handler.matchPoseForDataset(trainerPose, traineePose, id);
                    if(frameResult != null) {
                        ConstantUtilities.jedis.lpush("video_" + id, mapper.writeValueAsString(frameResult));
                    }
                    else {
                        ConstantUtilities.jedis.lpush("video_" + id, "none");
                    }
                }
                else {
                    String poseForSuggestion = handler.matchPoseForPreSuggest(trainerPose, traineePose);
                    if(poseForSuggestion != null) {
                        ConstantUtilities.jedis.lpush("preSuggestion_" + id, poseForSuggestion);
                    }
                    else {
                        ConstantUtilities.jedis.lpush("preSuggestion_" + id, "none");
                    }
                }

                //Luu ket qua so sanh 2 frame vao jedis




        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/matchVideo", method = RequestMethod.POST)
    public void compareImageLists(@RequestBody String jsonImageLists) {
        List<SuggestionDetail> finalResult;
        ObjectMapper mapper = new ObjectMapper();
        List<Object> imageLists;
        try {
            imageLists = mapper.readValue(jsonImageLists, List.class);
            List<String> imgList = (List<String>) imageLists.get(0);
            String suggestionId = (String) imageLists.get(1);
            //can than ep kieu
            int videoTrainerId = Integer.parseInt((String)imageLists.get(2));
            String traineeFolder = (String) imageLists.get(3);
            //
            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<Frame[]> responseEntity = restTemplate.getForEntity("http://localhost:8080/frame/getFramesByVideoId?videoId=" + videoTrainerId, Frame[].class);
            Frame[] frames = responseEntity.getBody();
            List<Frame> frameList = Arrays.asList(frames);

//            List<Frame> frameList = restTemplate.getForObject("http://localhost:8080/frame/getFramesByVideoId?videoId=" + videoTrainerId, List.class);
            //

            PoseMatchingHandler handler = new PoseMatchingHandler();
            finalResult = handler.makeSuggestionDetails(frameList, imgList, suggestionId, traineeFolder);
            System.out.println("Size: " + finalResult.size());

//            MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
//            header.add("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsIkpXVEF1dGhvcml0aWVzS2V5IjoiQWRtaW4iLCJleHAiOjE1NTI3MzI0Mzh9.H3nyrh34NRPXz-UJAN_rnPG4td2SKOu1JS2cTkcbzFvlHxcWfHDrD2YWaU3VTWKU3wv2oRrBgYMsl23F8TjIyg");
//            HttpEntity<String> entity = new HttpEntity(finalResult, header);

//            ResponseEntity<String> result = restTemplate.exchange("http://localhost:8080/suggestiondetail/createSuggestionDetails", HttpMethod.POST, entity, String.class);
            String result = restTemplate.postForObject("http://localhost:8080/suggestiondetail/createSuggestionDetails", finalResult, String.class);

            System.out.println("Start Time: " + ConstantUtilities.startTime);
            System.out.println("End Time: " + LocalDateTime.now());
//            for (int i = 0; i < finalResult.size(); i++) {
//                System.out.println(finalResult.get(i));
//                System.out.println("\n===============\n");
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //
    @RequestMapping(value = "/createDataset", method = RequestMethod.POST)
    public void compareFramesInDataset(@RequestBody String jsonImageLists) {
        List<Frame> dataset;

        ObjectMapper mapper = new ObjectMapper();
        List<Object> imageLists;
        try {
            imageLists = mapper.readValue(jsonImageLists, List.class);
            List<String> imgList = (List<String>) imageLists.get(0);
            String videoId = (String) imageLists.get(1);
            String trainerFolder = (String) imageLists.get(2);

            PoseMatchingHandler handler = new PoseMatchingHandler();
//            finalResult = handler.makeSuggestionDetails(simgList, imgList, suggestionId, trainerFolder, traineeFolder);
//            System.out.println("Size: " + finalResult.size());
            dataset = handler.createDataset(imgList, videoId, trainerFolder);
            System.out.println("Size: " + dataset.size());
            RestTemplate restTemplate = new RestTemplate();

            String result = restTemplate.postForObject("http://localhost:8080/frame/createDataset", dataset, String.class);

            System.out.println("Start Time: " + ConstantUtilities.startTime);
            System.out.println("End Time: " + LocalDateTime.now());
//            for (int i = 0; i < finalResult.size(); i++) {
//                System.out.println(finalResult.get(i));
//                System.out.println("\n===============\n");
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
