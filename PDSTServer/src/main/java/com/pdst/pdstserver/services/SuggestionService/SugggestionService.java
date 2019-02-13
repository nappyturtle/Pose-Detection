package com.pdst.pdstserver.services.SuggestionService;

import com.pdst.pdstserver.models.Suggestion;
import com.pdst.pdstserver.models.SuggestionTemp;

import java.util.List;

public interface SugggestionService  {
    List<SuggestionTemp> getAllSuggestions();
}
