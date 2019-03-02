package com.pdst.pdstserver.services.SuggestionService;

import com.pdst.pdstserver.dtos.SuggestionDTO;

import java.util.List;

public interface SuggestionService {
    List<SuggestionDTO> getAllSuggestions();
    boolean createSuggestion(SuggestionDTO suggestion);
	List<SuggestionDTO> getSuggestionByTrainee(int page, int size, int id);
	boolean updateSuggestionStatus(int id, String status);
}
