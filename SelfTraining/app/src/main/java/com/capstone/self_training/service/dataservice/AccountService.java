package com.capstone.self_training.service.dataservice;



import android.content.Context;

import com.capstone.self_training.model.Account;
import com.capstone.self_training.service.iService.IAccountService;
import com.capstone.self_training.util.Constants;

import java.io.IOException;

import retrofit2.Call;


public class AccountService {
    private IAccountService iAccountService;
    private Context context;
    public AccountService(Context context){
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
}
