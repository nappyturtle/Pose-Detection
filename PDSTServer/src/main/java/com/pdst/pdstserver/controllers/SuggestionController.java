package com.pdst.pdstserver.controllers;

import com.pdst.pdstserver.dtos.SuggestionDTO;
import com.pdst.pdstserver.services.SuggestionService.SugggestionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public List<SuggestionDTO> getAllSuggestions() {

        return sugggestionService.getAllSuggestions();
    }

    @PostMapping(value = "/create")
    public ResponseEntity<Void> createSuggestion(@RequestBody SuggestionDTO suggestion) {
        boolean flag = sugggestionService.createSuggestion(suggestion);
        if (flag == false) {
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    @GetMapping("suggestionsByTrainee")
    public List<SuggestionDTO> getSuggestionByTrainee(@RequestParam(value = "id") int id){
        return sugggestionService.getSuggestionByTrainee(id);
    }
}
