package com.pdst.pdstserver.controllers;

import com.pdst.pdstserver.dtos.SuggestionDTO;
import com.pdst.pdstserver.models.Suggestion;
import com.pdst.pdstserver.models.SuggestionTest;
import com.pdst.pdstserver.models.Video;
import com.pdst.pdstserver.services.SuggestionService.SugggestionService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

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
    public ResponseEntity<Void> createSuggestion(@RequestBody SuggestionTest suggestion, UriComponentsBuilder builder) {
        boolean flag = sugggestionService.createSuggestion(suggestion);
        if (flag == false) {
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.path("create/{name}").buildAndExpand(suggestion.getName()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }
}
