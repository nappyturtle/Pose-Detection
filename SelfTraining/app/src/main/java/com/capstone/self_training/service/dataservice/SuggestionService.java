package com.capstone.self_training.service.dataservice;

import android.util.Log;

import com.capstone.self_training.model.Suggestion;
import com.capstone.self_training.service.iService.ISuggestionService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class SuggestionService {
    private ISuggestionService iSuggestionService;

    public Call<Void> createSuggestion(String token,Suggestion suggestion) {
        iSuggestionService = DataService.getSuggestionService();
        Call<Void> call = iSuggestionService.createSuggestion(token,suggestion);
        try {
            call.execute().body();
        } catch (Exception e) {
            Log.e("SuggestionService createSuggestion = ",e.getMessage());
        }
        return null;
    }

    public List<Suggestion> getSuggestionList(String token,int page, int size,  int accountId) {
        iSuggestionService = DataService.getSuggestionService();
        List<Suggestion> suggestionList = null;
        Call<List<Suggestion>> callBack = iSuggestionService.getSuggestionList(token,page,size,accountId);
        try{
            suggestionList = callBack.execute().body();
        }catch (Exception e){
            Log.e("SuggestionService getSuggestionList = ",e.getMessage());
        }
        return suggestionList;
    }

    public List<Suggestion> getSuggestionListByTrainer(String token,int page, int size,  int trainerId, int traineeId) {
        iSuggestionService = DataService.getSuggestionService();
        List<Suggestion> suggestionList = null;
        Call<List<Suggestion>> callBack = iSuggestionService.getSuggestionByTrainer(token,page,size,trainerId,traineeId);
        try{
            suggestionList = callBack.execute().body();
        }catch (Exception e){
            Log.e("SuggestionService getSuggestionList = ",e.getMessage());
        }
        return suggestionList;
    }
}
