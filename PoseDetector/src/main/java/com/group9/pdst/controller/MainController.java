package com.group9.pdst.controller;

import com.group9.pdst.utils.OpenBrowserUtilities;
import org.springframework.web.bind.annotation.*;


@RestController
public class MainController {

    @RequestMapping(value = "/getPairOfImageLists", method = RequestMethod.GET)
    public void getPairOfImageLists(@RequestParam("listImg") String listImg, @RequestParam("listSimg") String listSimg, @RequestParam("suggestionId") String suggestionId) {
        OpenBrowserUtilities.openBrowser("http://localhost:8080/getImageLists.html?listImg=" + listImg + "&listSimg=" + listSimg + "&suggestionId=" + suggestionId);
    }
}
