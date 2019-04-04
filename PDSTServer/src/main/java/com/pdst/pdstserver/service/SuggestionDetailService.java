package com.pdst.pdstserver.service;

import com.pdst.pdstserver.dto.SuggestionDetailDTOFrontEnd;
import com.pdst.pdstserver.model.SuggestionDetail;

import java.util.List;

public interface SuggestionDetailService {
    List<SuggestionDetail> getAllSuggestionDetails();
    List<SuggestionDetail> getSuggestionDetails(String field, Object value);
    SuggestionDetail createSuggestionDetail(SuggestionDetail suggestionDetail);
    Boolean saveComment(SuggestionDetail suggestionDetail);
    List<SuggestionDetailDTOFrontEnd> getAllSuggestionDetailByStaffOrAdmin(int suggestionId);
    boolean editStatusSuggestionDetailByStaffOrAdmin(SuggestionDetailDTOFrontEnd dto);
    SuggestionDetailDTOFrontEnd getSuggestionDetailById(int suggestionDetailId);
}
