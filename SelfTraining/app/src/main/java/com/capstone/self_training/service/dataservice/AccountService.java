package com.capstone.self_training.service.dataservice;

import com.capstone.self_training.model.Account;
import com.capstone.self_training.service.iService.IAccountService;

import retrofit2.Call;

public class AccountService {
    private IAccountService iAccountService;

    public Call<Void> register(Account account) {
        iAccountService = DataService.getAccountService();
        Call<Void> call = iAccountService.register(account);
        try {
            call.execute().body();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
