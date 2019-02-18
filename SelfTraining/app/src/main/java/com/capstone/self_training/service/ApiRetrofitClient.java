package com.capstone.self_training.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiRetrofitClient {
    private static Retrofit retrofit = null;
    public static Retrofit getClient(String baseUrl){
        OkHttpClient okHttpClient = new OkHttpClient
                .Builder()
                .readTimeout(1, TimeUnit.MINUTES) // ngắt thời gian đọc từ server
                .writeTimeout(1,TimeUnit.MINUTES)
                .connectTimeout(1,TimeUnit.MINUTES) // thời gian ngắt kết nối
                .retryOnConnectionFailure(true) // nếu lỗi về mạng thì nó sẽ cố gắng kết nối lai
                .protocols(Arrays.asList(Protocol.HTTP_1_1)) // trường hợp ko kiếm dc giao thức
                                                   // set lại giao thức cho nó
                .build();

        Gson gson = new GsonBuilder().setLenient().create();
        // dùng để mapping các field trong Json thành các field trong java, kèm với
        // đó là có các annotation
        retrofit = new Retrofit.Builder()
                                .baseUrl(baseUrl).client(okHttpClient)
                                .addConverterFactory(GsonConverterFactory.create(gson))
                                // mapping field trong json thành các field trong java
                                .build();
        return retrofit;
    }
}
