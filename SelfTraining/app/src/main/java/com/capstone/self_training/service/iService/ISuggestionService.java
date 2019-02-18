package com.capstone.self_training.service.iService;

import com.capstone.self_training.model.Suggestion;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ISuggestionService {
    @POST("suggestion/create")
    Call<Void> createSuggestion(@Body Suggestion suggestion);
}
