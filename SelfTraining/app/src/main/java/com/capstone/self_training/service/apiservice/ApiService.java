package com.capstone.self_training.service.apiservice;

import com.capstone.self_training.model.Suggestion;
import com.capstone.self_training.model.SuggestionDetail;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("suggestiondetail/getSuggestionDetailsBySuggestion")
    Call<List<SuggestionDetail>> getSuggestionDetail(@Query("suggestionId") int suggestionId);

    @GET("suggestion/suggestions")
    Call<List<Suggestion>> getSuggestionList();


}
