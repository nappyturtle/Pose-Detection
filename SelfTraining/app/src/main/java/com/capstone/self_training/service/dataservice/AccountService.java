package com.capstone.self_training.service.dataservice;


import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;

import com.capstone.self_training.dto.TrainerInfo;
import com.capstone.self_training.model.Account;
import com.capstone.self_training.service.iService.IAccountService;
import com.capstone.self_training.util.Constants;

import java.io.IOException;

import retrofit2.Call;


public class AccountService {
    private IAccountService iAccountService;
    private Context context;

    public AccountService(Context context) {
        this.context = context;
    }

    public Account login(Account account) {
        iAccountService = DataService.getAccountService();
        Call<Account> callBack = iAccountService.login(account);
        Account accountResponse = null;

        try {
            int status = callBack.clone().execute().code();
            if (status == Constants.Status_Forbidden) {
                if (callBack.execute().body() == null) {
                    return null;
                }
            } else if (status == Constants.Status_Ok) {
                accountResponse = callBack.execute().body();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return accountResponse;
    }

    public Account getAccount(int id) {
        iAccountService = DataService.getAccountService();
        Call<Account> call = iAccountService.getAccount(id);
        Account account = null;
        try {
            account = call.execute().body();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return account;
    }

    public Integer register(Account account) {
        iAccountService = DataService.getAccountService();
        Call<Integer> call = iAccountService.register(account);
        Integer status = 0;
        try {
            status = call.execute().code();
//            if (status == Constants.Status_Conflit) {
//                Toast.makeText(context,String.valueOf(call.execute().body()), Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(context,String.valueOf(call.execute().body()), Toast.LENGTH_SHORT).show();
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }

    public void updateProfile(Account account) {
        iAccountService = DataService.getAccountService();
        Call<Void> call = iAccountService.updateProfile(account);
        String mess = null;
        try {
            call.execute().body();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TrainerInfo getTrainerInfo(int accountId) {
        iAccountService = DataService.getAccountService();
        Call<TrainerInfo> call = iAccountService.getTrainerInfo(accountId);
        TrainerInfo trainerInfo = new TrainerInfo();
        try {
            trainerInfo = call.execute().body();
        } catch (Exception e) {
            Log.e("AccountService getTrainerInfo: ", e.getMessage());
        }
        return trainerInfo;
    }
}
