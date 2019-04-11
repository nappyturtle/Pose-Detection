package com.pdst.pdstserver.impl;


import com.pdst.pdstserver.model.Category;
import com.pdst.pdstserver.repository.CategoryRepository;
import com.pdst.pdstserver.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCateById(int cateId) {
        return categoryRepository.findCategoryById(cateId);
    }

    @Override
    public boolean checkExistCateName(String catename) {
        Category existedCategory = categoryRepository.findCategoryByName(catename);
        if (existedCategory == null) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public Category updateCate(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category createCate(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public int countTotalCategories() {
        return categoryRepository.countTotalCategories();
    }

    @Override
    public Category getCateByCateName(String catename) {
        return categoryRepository.findCategoryByName(catename);
    }
}
