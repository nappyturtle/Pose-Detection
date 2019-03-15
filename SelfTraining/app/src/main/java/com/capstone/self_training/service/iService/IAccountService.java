package com.capstone.self_training.service.iService;

import com.capstone.self_training.dto.TrainerInfo;
import com.capstone.self_training.model.Account;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IAccountService {
    @POST("login")
    Call<Account> login(@Body Account account);

    @POST("account/sign-up")
    Call<Integer> register(@Body Account account);

    @GET("account/update/{id}")
    Call<Account> getAccount(@Path("id") int id);

    @PUT("account/edit")
    Call<Void> updateProfile(@Body Account account);

    @GET("account/getTrainerInfo")
    Call<TrainerInfo> getTrainerInfo(@Query(value = "accountId") int accountId);
}
