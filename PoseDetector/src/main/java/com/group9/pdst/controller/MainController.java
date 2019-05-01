package com.group9.pdst.controller;

import com.group9.pdst.utils.ConstantUtilities;
import com.group9.pdst.utils.OpenBrowserUtilities;
import org.springframework.web.bind.annotation.*;


@RestController
public class MainController {

    @RequestMapping(value = "/getPairOfImageLists", method = RequestMethod.GET)
    public void getPairOfImageLists(@RequestParam("listImg") String listImg, @RequestParam("videoTrainerId") String videoTrainerId, @RequestParam("suggestionId") String suggestionId) {
        OpenBrowserUtilities.openBrowser(ConstantUtilities.domain + "getImageLists.html?listImg=" + listImg + "&videoTrainerId=" + videoTrainerId + "&suggestionId=" + suggestionId);
    }

    @RequestMapping(value = "/getDataset", method = RequestMethod.GET)
    public void getDataset(@RequestParam("listImg") String listImg, @RequestParam("videoId") String videoId) {
        OpenBrowserUtilities.openBrowser(ConstantUtilities.domain + "getDatasetList.html?listImg=" + listImg + "&videoId=" + videoId);
    }

}
