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
}
