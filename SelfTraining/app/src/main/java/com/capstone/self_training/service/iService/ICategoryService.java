package com.capstone.self_training.service.iService;

import com.capstone.self_training.model.Category;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ICategoryService {
    @GET("category/categories")
    Call<List<Category>> getAllcategories();
}
