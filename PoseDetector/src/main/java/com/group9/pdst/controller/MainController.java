package com.group9.pdst.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group9.pdst.handler.PoseMatchingHandler;
import com.group9.pdst.model.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;


@Controller
public class MainController {
    @RequestMapping(value = "/getPairOfImages", method = RequestMethod.GET)
    public String welcome(@RequestParam("img") String img, @RequestParam("simg") String simg, ModelMap model) {
        model.addAttribute("img", img);
        model.addAttribute("simg", simg);
        return "poseDetect";
    }
}
