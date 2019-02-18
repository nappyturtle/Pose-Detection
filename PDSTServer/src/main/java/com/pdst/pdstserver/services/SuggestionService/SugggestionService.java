package com.pdst.pdstserver.services.SuggestionService;

import com.pdst.pdstserver.dtos.SuggestionDTO;
import com.pdst.pdstserver.models.Suggestion;
import com.pdst.pdstserver.models.Video;

import java.util.List;

public interface SugggestionService  {
    List<SuggestionDTO> getAllSuggestions();
    boolean createSuggestion(Suggestion suggestion);
}
