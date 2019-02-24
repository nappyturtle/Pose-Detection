package com.capstone.self_training.service.dataservice;

import com.capstone.self_training.service.ApiRetrofitClient;
import com.capstone.self_training.service.iService.IAccountService;
import com.capstone.self_training.service.iService.ISuggestionDetailService;
import com.capstone.self_training.service.iService.IAccountService;
import com.capstone.self_training.service.iService.ICategoryService;
import com.capstone.self_training.service.iService.ISuggestionService;
import com.capstone.self_training.service.iService.IVideoService;
import com.capstone.self_training.util.Constants;

public class DataService {
    private static String baseUrl = "http://" + Constants.domain + Constants.port;

    public static IVideoService getVideoService() {
        return ApiRetrofitClient.getClient(baseUrl).create(IVideoService.class);
    }

    public static ISuggestionService getSuggestionService() {
        return ApiRetrofitClient.getClient(baseUrl).create(ISuggestionService.class);
    }

    public static ISuggestionDetailService getSuggestionDetailService() {
        return ApiRetrofitClient.getClient(baseUrl).create(ISuggestionDetailService.class);
    }

    public static ICategoryService getCategoryService() {
        return ApiRetrofitClient.getClient(baseUrl).create((ICategoryService.class));
    }


    public static IAccountService getAccountService() {
        return ApiRetrofitClient.getClient(baseUrl).create((IAccountService.class));
    }
}
