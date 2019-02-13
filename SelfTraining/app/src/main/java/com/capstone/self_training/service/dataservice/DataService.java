package com.capstone.self_training.service.dataservice;

import com.capstone.self_training.service.ApiRetrofitClient;
import com.capstone.self_training.service.apiservice.ApiService;
import com.capstone.self_training.util.Constants;

public class DataService {
    private static String baseUrl = "http://"+ Constants.domain+Constants.port;
    public static ApiService getService(){
        return ApiRetrofitClient.getClient(baseUrl).create(ApiService.class);
    }
}
