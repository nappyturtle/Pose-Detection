package com.capstone.self_training.service.iService;

import com.capstone.self_training.model.SuggestionDetail;
import com.capstone.self_training.util.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ISuggestionDetailService {
    @GET("suggestiondetail/getSuggestionDetailsBySuggestion")
    Call<List<SuggestionDetail>> getSuggestionDetail(@Header(Constants.header_string) String token,
                                                     @Query("suggestionId") int suggestionId);

    @PUT("suggestiondetail/saveComment")
    Call<Boolean> saveComment(@Header(Constants.header_string) String token, @Body SuggestionDetail suggestionDetail);


}
