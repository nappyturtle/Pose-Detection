package com.pdst.pdstserver.controllers;

import com.pdst.pdstserver.services.suggestiondetailservice.SuggestionDetailServcie;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(SuggestionDetailController.BASE_URL)
public class SuggestionDetailController {
    public static final String BASE_URL = "suggestiondetail";
    private final SuggestionDetailServcie suggestionDetailServcie;
    public SuggestionDetailController(SuggestionDetailServcie suggestionDetailServcie) {
        this.suggestionDetailServcie = suggestionDetailServcie;
    }

}
