package com.group9.pdst.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group9.pdst.handler.PoseMatchingHandler;
import com.group9.pdst.model.MatchingPoseResult;
import com.group9.pdst.model.Pose;
import com.group9.pdst.model.SuggestionDetail;
import com.group9.pdst.utils.CalculationUtilities;
import com.group9.pdst.utils.ConstantUtilities;
import com.group9.pdst.utils.OpenBrowserUtilities;
import org.springframework.web.bind.annotation.*;
import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class PoseMatchingController {


    @RequestMapping(value = "/matchPose", method = RequestMethod.POST)
    public void comparePose(@RequestBody String jsonPoses) {
        String result = "";
        ObjectMapper mapper = new ObjectMapper();
        List<String> poses;
        try {
            poses = mapper.readValue(jsonPoses, List.class);
            Pose trainerPose = mapper.readValue(poses.get(0), Pose.class);
            Pose traineePose = mapper.readValue(poses.get(1), Pose.class);
            String suggestionId = poses.get(2);
            PoseMatchingHandler handler = new PoseMatchingHandler();
            result = handler.matchPose(trainerPose, traineePose);
//            System.out.println(result);
            ConstantUtilities.jedis.lpush(suggestionId, result);
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
            PoseMatchingHandler handler = new PoseMatchingHandler();
            finalResult = handler.makeSuggestionDetails(simgList, imgList, suggestionId);
            for (int i = 0; i < finalResult.size(); i++) {
                System.out.println(finalResult.get(i));
                System.out.println("\n===============\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
