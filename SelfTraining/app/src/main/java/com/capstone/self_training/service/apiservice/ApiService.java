package com.capstone.self_training.service.apiservice;

import com.capstone.self_training.model.Suggestion;
import com.capstone.self_training.model.SuggestionDetail;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("")
    Call<List<SuggestionDetail>> getSuggestionDetail();

    @GET("")
    Call<List<Suggestion>> getSuggestionList();
}
