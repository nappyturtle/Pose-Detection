package com.capstone.self_training.service;


import android.widget.BaseAdapter;


import com.capstone.self_training.service.iService.ISuggestionService;
import com.capstone.self_training.service.iService.IVideoService;
import com.capstone.self_training.util.Constants;

import retrofit2.Retrofit;

public class ApiUtils {
    public static final String BASE_URL = "http://"+ Constants.domain+Constants.port;;

    public static IVideoService getVideoService(){
        return ApiRetrofitClient.getClient(BASE_URL).create(IVideoService.class);
    }

    public static ISuggestionService getSuggestionService(){
        return ApiRetrofitClient.getClient(BASE_URL).create(ISuggestionService.class);
    }

}

