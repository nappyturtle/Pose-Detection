package com.capstone.self_training.service.iService;

import com.capstone.self_training.model.SuggestionTurnTransaction;
import com.capstone.self_training.util.Constants;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ISuggestionTurnTransactionService {

    @POST("suggestionturn/buySuggestionTurn")
    Call<SuggestionTurnTransaction> buySuggestionTurn(@Header(Constants.header_string) String token,
                                                      @Body SuggestionTurnTransaction suggestionTurnTransaction);

}
