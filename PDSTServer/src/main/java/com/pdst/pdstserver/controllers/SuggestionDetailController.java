package com.pdst.pdstserver.controllers;

import com.pdst.pdstserver.models.SuggestionDetail;
import com.pdst.pdstserver.services.suggestiondetailservice.SuggestionDetailServcie;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(SuggestionDetailController.BASE_URL)
public class SuggestionDetailController {
    public static final String BASE_URL = "suggestiondetail";
    private final SuggestionDetailServcie suggestionDetailServcie;
    public SuggestionDetailController(SuggestionDetailServcie suggestionDetailServcie) {
        this.suggestionDetailServcie = suggestionDetailServcie;
    }
    @GetMapping("getAllSuggestionDetails")
    public List<SuggestionDetail> getAllSuggestionDetails() {
        return suggestionDetailServcie.getAllSuggestionDetails();
    }
    @GetMapping("getSuggestionDetailsBySuggestion")
    public List<SuggestionDetail> getSuggestionDetails(int suggestionId) {
        return suggestionDetailServcie.getSuggestionDetails("suggestionId", suggestionId);
    }

    @PostMapping("createSuggestionDetails")
    public String createSuggestionDetails(@RequestBody List<SuggestionDetail> suggestionDetails) {
        for (int i = 0; i < suggestionDetails.size(); i++) {
            SuggestionDetail suggestionDetail =  suggestionDetails.get(i);
            suggestionDetail.setCreatedTime(LocalDateTime.now().toString());
            suggestionDetailServcie.createSuggestionDetail(suggestionDetail);
        }
        return "Success";
    }
}
