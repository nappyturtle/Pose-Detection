package com.pdst.pdstserver.controllers;

import com.pdst.pdstserver.models.SuggestionDetail;
import com.pdst.pdstserver.services.SuggestionService.SuggestionService;
import com.pdst.pdstserver.services.suggestiondetailservice.SuggestionDetailService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(SuggestionDetailController.BASE_URL)
public class SuggestionDetailController {
    public static final String BASE_URL = "suggestiondetail";
    private final SuggestionDetailService suggestionDetailService;
    private final SuggestionService suggestionService;

    public SuggestionDetailController(SuggestionDetailService suggestionDetailService, SuggestionService suggestionService) {
        this.suggestionDetailService = suggestionDetailService;
        this.suggestionService = suggestionService;
    }
    @GetMapping("getAllSuggestionDetails")
    public List<SuggestionDetail> getAllSuggestionDetails() {
        return suggestionDetailService.getAllSuggestionDetails();
    }
    @GetMapping("getSuggestionDetailsBySuggestion")
    public List<SuggestionDetail> getSuggestionDetails(int suggestionId) {
        return suggestionDetailService.getSuggestionDetails("suggestionId", suggestionId);
    }

    @PostMapping("createSuggestionDetails")
    public String createSuggestionDetails(@RequestBody List<SuggestionDetail> suggestionDetails) {
        for (int i = 0; i < suggestionDetails.size(); i++) {
            SuggestionDetail suggestionDetail =  suggestionDetails.get(i);
            suggestionDetail.setCreatedTime(LocalDateTime.now().toString());
            suggestionDetailService.createSuggestionDetail(suggestionDetail);
        }
        suggestionService.updateSuggestionStatus(suggestionDetails.get(0).getSuggestionId(), "active");
//        suggestionService
        return "Success";
    }
}
