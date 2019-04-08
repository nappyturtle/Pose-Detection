package com.pdst.pdstserver.service;

import com.pdst.pdstserver.model.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();

    Category getCateById(int cateId);

    boolean checkExistCateName(String catename);

    Category updateCate(Category category);

    Category createCate(Category category);

    int countTotalCategories();
}
