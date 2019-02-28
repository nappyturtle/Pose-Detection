package com.pdst.pdstserver.services.suggestionService;

import com.pdst.pdstserver.dtos.SuggestionDTO;

import java.util.List;

public interface SuggestionService {
    List<SuggestionDTO> getAllSuggestions();
    boolean createSuggestion(SuggestionDTO suggestion);
	List<SuggestionDTO> getSuggestionByTrainee(int id);
	boolean updateSuggestionStatus(int id, String status);
}
