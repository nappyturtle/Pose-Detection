package com.pdst.pdstserver.services.suggestiondetailservice;

import com.pdst.pdstserver.models.SuggestionDetail;
import com.pdst.pdstserver.repositories.SuggestionDetailRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SuggestionDetailServcieImpl implements SuggestionDetailServcie {

    private final SuggestionDetailRepository suggestionDetailRepository;

    public SuggestionDetailServcieImpl(SuggestionDetailRepository suggestionDetailRepository) {
        this.suggestionDetailRepository = suggestionDetailRepository;
    }

    @Override
    public List<SuggestionDetail> getAllSuggestionDetails() {
        return suggestionDetailRepository.findAll();
    }
}
