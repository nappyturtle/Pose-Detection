package com.capstone.self_training.service.dataservice;

import android.util.Log;

import com.capstone.self_training.model.SuggestionDetail;
import com.capstone.self_training.service.iService.ISuggestionDetailService;

import java.util.List;

import retrofit2.Call;

public class SuggestionDetailService {
    private ISuggestionDetailService iSuggestionDetailService;

    public List<SuggestionDetail> getSuggestionDetailList(String token, int suggestionId) {
        iSuggestionDetailService = DataService.getSuggestionDetailService();
        List<SuggestionDetail> detailList = null;
        Call<List<SuggestionDetail>> listCall = iSuggestionDetailService.getSuggestionDetail(token, suggestionId);
        try {
            detailList = listCall.execute().body();
        } catch (Exception e) {
            Log.e("SuggestionDetailService getSuggestionDetailList: ",e.getMessage());
        }
        return detailList;
    }
}
