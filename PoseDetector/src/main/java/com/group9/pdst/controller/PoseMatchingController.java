package com.group9.pdst.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group9.pdst.handler.PoseMatchingHandler;
import com.group9.pdst.model.Pose;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.io.IOException;
@RestController
public class PoseMatchingController {
    @RequestMapping(value = "/matchPose", method = RequestMethod.POST)
    public String comparePose(@RequestBody String jsonPoses, HttpSession session) {
        String result = "";
        ObjectMapper mapper = new ObjectMapper();

        Pose[] poses = new Pose[2];
        try {
            poses = mapper.readValue(jsonPoses, Pose[].class);
            Pose trainerPose = poses[0];
            Pose traineePose = poses[1];
            PoseMatchingHandler handler = new PoseMatchingHandler();
            result = handler.matchPose(trainerPose, traineePose);
            session.setAttribute("matchingResult" , result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
