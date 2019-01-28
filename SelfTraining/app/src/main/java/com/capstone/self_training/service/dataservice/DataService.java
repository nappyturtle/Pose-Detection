package com.capstone.self_training.service.dataservice;

import com.capstone.self_training.service.ApiRetrofitClient;
import com.capstone.self_training.service.apiservice.ApiService;

public class DataService {
    private static String baseUrl = "";
    public static ApiService getService(){
        return ApiRetrofitClient.getClient(baseUrl).create(ApiService.class);
    }
}
