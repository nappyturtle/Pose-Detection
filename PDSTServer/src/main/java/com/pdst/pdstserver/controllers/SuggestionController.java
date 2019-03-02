package com.pdst.pdstserver.controllers;

import com.pdst.pdstserver.dtos.SuggestionDTO;
import com.pdst.pdstserver.services.SuggestionService.SuggestionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(SuggestionController.BASE_URL)
public class SuggestionController {
    public static final String BASE_URL = "suggestion";

    private final SuggestionService suggestionService;

    public SuggestionController(SuggestionService suggestionService) {
        this.suggestionService = suggestionService;
    }

    @GetMapping("suggestions")
    public List<SuggestionDTO> getAllSuggestions() {

        return suggestionService.getAllSuggestions();
    }

    @PostMapping(value = "/create")
    public ResponseEntity<Void> createSuggestion(@RequestBody SuggestionDTO suggestion) {
        boolean flag = suggestionService.createSuggestion(suggestion);
        if (flag == false) {
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    @GetMapping("suggestionsByTrainee")
    public List<SuggestionDTO> getSuggestionByTrainee(@RequestParam(value = "page") int page, @RequestParam(value = "size") int size, @RequestParam(value = "id") int id) {

        return suggestionService.getSuggestionByTrainee(page,size,id);
    }
}
