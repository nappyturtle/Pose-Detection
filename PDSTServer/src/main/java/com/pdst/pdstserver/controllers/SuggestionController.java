package com.pdst.pdstserver.controllers;

import com.pdst.pdstserver.models.Suggestion;
import com.pdst.pdstserver.models.SuggestionTemp;
import com.pdst.pdstserver.services.SuggestionService.SugggestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(SuggestionController.BASE_URL)
public class SuggestionController {
    public static final String BASE_URL = "suggestion";

    private final SugggestionService sugggestionService;

    public SuggestionController(SugggestionService sugggestionService) {
        this.sugggestionService = sugggestionService;
    }
    @GetMapping("suggestions")
    public List<SuggestionTemp> getAllSuggestions() {
        return sugggestionService.getAllSuggestions();
    }
}
