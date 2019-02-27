package com.pdst.pdstserver.services.SuggestionService;

import com.pdst.pdstserver.dtos.SuggestionDTO;
import com.pdst.pdstserver.models.Suggestion;

import java.util.List;

public interface SugggestionService  {
    List<SuggestionDTO> getAllSuggestions();
    boolean createSuggestion(SuggestionDTO suggestion);
	List<SuggestionDTO> getSuggestionByTrainee(int id);
}
