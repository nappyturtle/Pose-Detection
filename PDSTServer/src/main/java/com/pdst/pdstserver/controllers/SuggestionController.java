package com.pdst.pdstserver.controllers;

import com.pdst.pdstserver.services.SuggestionService.SugggestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(SuggestionController.BASE_URL)
public class SuggestionController {
    public static final String BASE_URL = "suggestion";

    private final SugggestionService sugggestionService;

    public SuggestionController(SugggestionService sugggestionService) {
        this.sugggestionService = sugggestionService;
    }
}
