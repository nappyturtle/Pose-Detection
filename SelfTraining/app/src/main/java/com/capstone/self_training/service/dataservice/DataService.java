package com.capstone.self_training.service.dataservice;

import com.capstone.self_training.service.ApiRetrofitClient;
import com.capstone.self_training.service.apiservice.ApiService;
import com.capstone.self_training.service.iService.ICategoryService;
import com.capstone.self_training.service.iService.ISuggestionService;
import com.capstone.self_training.service.iService.IVideoService;
import com.capstone.self_training.util.Constants;

public class DataService {
    private static String baseUrl = "http://"+ Constants.domain+Constants.port;
    public static ApiService getService(){
        return ApiRetrofitClient.getClient(baseUrl).create(ApiService.class);
    }
    public static IVideoService getVideoService(){
        return ApiRetrofitClient.getClient(baseUrl).create(IVideoService.class);
    }

    public static ISuggestionService getSuggestionService(){
        return ApiRetrofitClient.getClient(baseUrl).create(ISuggestionService.class);
    }
    public static ICategoryService getCategoryService() {
        return ApiRetrofitClient.getClient(baseUrl).create((ICategoryService.class));
    }
}
