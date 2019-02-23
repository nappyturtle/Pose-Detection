package com.capstone.self_training.service.iService;

import com.capstone.self_training.model.Account;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IAccountService {
    @POST("account/register")
    Call<Void> register(@Body Account account);
}
