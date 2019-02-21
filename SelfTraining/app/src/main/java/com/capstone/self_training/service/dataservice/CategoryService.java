package com.capstone.self_training.service.dataservice;

import com.capstone.self_training.model.Category;
import com.capstone.self_training.service.ApiRetrofitClient;
import com.capstone.self_training.service.iService.ICategoryService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class CategoryService {

    private ICategoryService iCategoryService;

    public List<Category> getAllcategories() {
        iCategoryService = DataService.getCategoryService();
        List<Category> categoryList = null;
        Call<List<Category>> call = iCategoryService.getAllcategories();
        try {
            categoryList = call.execute().body();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categoryList;
    }
}
