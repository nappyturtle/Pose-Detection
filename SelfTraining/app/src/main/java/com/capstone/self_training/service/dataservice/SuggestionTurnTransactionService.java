package com.capstone.self_training.service.dataservice;


import android.util.Log;

import com.capstone.self_training.model.SuggestionTurnTransaction;
import com.capstone.self_training.service.iService.ISuggestionTurnTransactionService;

import retrofit2.Call;

public class SuggestionTurnTransactionService {
    private ISuggestionTurnTransactionService iSuggestionTurnTransactionService;

    public boolean buySuggestionTurn(String token, SuggestionTurnTransaction suggestionTurnTransaction) {
        iSuggestionTurnTransactionService = DataService.getSuggestionTurnTransactionService();
        SuggestionTurnTransaction createdTransaction;
        Call<SuggestionTurnTransaction> call = iSuggestionTurnTransactionService.buySuggestionTurn(token, suggestionTurnTransaction);
        try {
            createdTransaction = call.execute().body();
            if (createdTransaction.getId() == 0) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            Log.e("Suggestion Turn buy = ", e.getMessage());
        }
        return false;
    }
}
