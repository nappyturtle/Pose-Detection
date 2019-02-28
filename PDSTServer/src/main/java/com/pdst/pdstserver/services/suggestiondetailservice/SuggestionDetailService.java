package com.pdst.pdstserver.services.suggestiondetailservice;

import com.pdst.pdstserver.models.SuggestionDetail;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SuggestionDetailService {
    List<SuggestionDetail> getAllSuggestionDetails();
    List<SuggestionDetail> getSuggestionDetails(String field, Object value);
    SuggestionDetail createSuggestionDetail(SuggestionDetail suggestionDetail);
}
