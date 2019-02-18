package com.capstone.self_training.service.dataservice;

import com.capstone.self_training.model.Suggestion;
import com.capstone.self_training.service.ApiUtils;
import com.capstone.self_training.service.iService.ISuggestionService;

import retrofit2.Call;

public class SuggestionService implements ISuggestionService {
    private ISuggestionService iSuggestionService;

    @Override
    public Call<Void> createSuggestion(Suggestion suggestion) {
        iSuggestionService = ApiUtils.getSuggestionService();
        Call<Void> call = iSuggestionService.createSuggestion(suggestion);
        try {
            call.execute().body();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
