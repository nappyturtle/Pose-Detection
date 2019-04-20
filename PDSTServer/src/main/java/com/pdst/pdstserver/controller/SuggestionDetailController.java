package com.pdst.pdstserver.controller;

import com.pdst.pdstserver.service.SuggestionService;
import com.pdst.pdstserver.model.SuggestionDetail;
import com.pdst.pdstserver.dto.SuggestionDetailDTOFrontEnd;
import com.pdst.pdstserver.service.SuggestionDetailService;
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

    @GetMapping("getAllSuggestionDetailByStaffOrAdmin/{suggestionId}")
    public List<SuggestionDetailDTOFrontEnd> getAllSuggestionDetailByStaffOrAdmin(@PathVariable(value = "suggestionId") int suggestionId) {
        return suggestionDetailService.getAllSuggestionDetailByStaffOrAdmin(suggestionId);
    }


    @GetMapping("getSuggestionDetailById/{suggestionDetailId}")
    public SuggestionDetailDTOFrontEnd getSuggestionDetailById(@PathVariable(value = "suggestionDetailId") int suggestionDetailId) {
        return suggestionDetailService.getSuggestionDetailById(suggestionDetailId);
    }

    @PutMapping("editStatusSuggestionDetailByStaffOrAdmin")
    public boolean getAllSuggestionDetails(@RequestBody SuggestionDetailDTOFrontEnd dto) {
        return suggestionDetailService.editStatusSuggestionDetailByStaffOrAdmin(dto);
    }


    @GetMapping("getSuggestionDetailsBySuggestion")
    public List<SuggestionDetail> getSuggestionDetails(int suggestionId) {
        return suggestionDetailService.getSuggestionDetails("suggestionId", suggestionId);
    }

    @GetMapping("getSuggestionDetailsBySuggestionForTrainee")
    public List<SuggestionDetail> getSuggestionDetailsForTrainee(int suggestionId) {
        return suggestionDetailService.getSuggestionDetailsForTrainee("suggestionId", suggestionId);
    }

    @PostMapping("createSuggestionDetails")
    public String createSuggestionDetails(@RequestBody List<SuggestionDetail> suggestionDetails) {
        if(suggestionDetails.size() != 0) {
            for (int i = 0; i < suggestionDetails.size(); i++) {
                SuggestionDetail suggestionDetail =  suggestionDetails.get(i);
                suggestionDetail.setCreatedTime(LocalDateTime.now().toString());
                suggestionDetailService.createSuggestionDetail(suggestionDetail);
            }
            suggestionService.updateSuggestionStatus(suggestionDetails.get(0).getSuggestionId(), "active");
            return "Success";
        }
        else return "Video của bạn không hề khớp với video mẫu";

//        suggestionService

    }
    @PutMapping("saveComment")
    public boolean createComment(@RequestBody SuggestionDetail suggestionDetail){
        return suggestionDetailService.saveComment(suggestionDetail);
    }
}
