package com.pdst.pdstserver.services.SuggestionService;

import com.pdst.pdstserver.models.Suggestion;
import com.pdst.pdstserver.repositories.SuggestionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SugggestionServiceImpl implements SugggestionService {
    private final SuggestionRepository suggestionRepository;

    public SugggestionServiceImpl(SuggestionRepository suggestionRepository) {
        this.suggestionRepository = suggestionRepository;
    }

    @Override
    public List<Suggestion> getAllSuggestions() {
        return suggestionRepository.findAll();
    }
}
