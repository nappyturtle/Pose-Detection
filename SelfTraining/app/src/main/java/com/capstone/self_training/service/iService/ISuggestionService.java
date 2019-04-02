package com.capstone.self_training.service.iService;

import com.capstone.self_training.dto.SuggestionDTO;
import com.capstone.self_training.model.Suggestion;
import com.capstone.self_training.util.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ISuggestionService {
    @POST("suggestion/create")
    Call<Void> createSuggestion(@Header(Constants.header_string) String token,@Body Suggestion suggestion);

    @GET("suggestion/suggestionsByTrainee")
    Call<List<SuggestionDTO>> getSuggestionList(@Header(Constants.header_string) String token, @Query("page") int page,
                                                @Query("size") int size, @Query("id") int accountId);

    @GET("suggestion/getSuggestionByTrainer")
    Call<List<SuggestionDTO>> getSuggestionByTrainer(@Header(Constants.header_string) String token,@Query("page") int page,
                                             @Query("size") int size,@Query("trainerId") int trainerId
            ,@Query("traineeId") int traineeId);
}
