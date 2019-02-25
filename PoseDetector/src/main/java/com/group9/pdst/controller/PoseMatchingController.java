package com.group9.pdst.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group9.pdst.handler.PoseMatchingHandler;
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
import java.util.List;
import java.util.Map;

@RestController
public class PoseMatchingController {


    @RequestMapping(value = "/matchPose", method = RequestMethod.POST)
    public void comparePose(@RequestBody String jsonPoses) {
        MatchingPoseResult result;
        ObjectMapper mapper = new ObjectMapper();
        List<String> poses;
        try {
            poses = mapper.readValue(jsonPoses, List.class);
                Pose trainerPose = mapper.readValue(poses.get(0), Pose.class);
                Pose traineePose = mapper.readValue(poses.get(1), Pose.class);
                String suggestionId = poses.get(2);
                PoseMatchingHandler handler = new PoseMatchingHandler();

                result = handler.matchPose(trainerPose, traineePose, suggestionId);
                //Luu ket qua so sanh 2 frame vao jedis
                ConstantUtilities.jedis.lpush(suggestionId, mapper.writeValueAsString(result));
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
            List<String> simgList = (List<String>) imageLists.get(0);
            List<String> imgList = (List<String>) imageLists.get(1);
            String suggestionId = (String) imageLists.get(2);
            String trainerFolder = (String) imageLists.get(3);
            String traineeFolder = (String) imageLists.get(4);
            PoseMatchingHandler handler = new PoseMatchingHandler();
            finalResult = handler.makeSuggestionDetails(simgList, imgList, suggestionId, trainerFolder, traineeFolder);
            RestTemplate restTemplate = new RestTemplate();
            MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
            header.add("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsIkpXVEF1dGhvcml0aWVzS2V5IjoiQWRtaW4iLCJleHAiOjE1NTE4NjI4NDF9.4UJthhHzMJwjtlBtf6HtJ0HEOHMSDA6hn7uJBEtdT-z1KaTgExuZM1m0UFAWFIkszj6CxbdPAFjQB9avPXXTPg");
            HttpEntity<String> entity = new HttpEntity(finalResult, header);

            ResponseEntity<String> result = restTemplate.exchange("http://localhost:8080/suggestiondetail/createSuggestionDetails", HttpMethod.POST, entity, String.class);
//            String result = restTemplate.postForObject("http://localhost:8080/suggestiondetail/createSuggestionDetails", finalResult, String.class);
            System.out.println(result);
            ConstantUtilities.jedis.flushAll();
//            for (int i = 0; i < finalResult.size(); i++) {
//                System.out.println(finalResult.get(i));
//                System.out.println("\n===============\n");
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
