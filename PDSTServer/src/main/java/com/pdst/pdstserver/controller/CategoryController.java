package com.pdst.pdstserver.controller;

import com.pdst.pdstserver.model.Category;
import com.pdst.pdstserver.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(CategoryController.BASE_URL)
public class CategoryController {
    public static final String BASE_URL = "category";

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("categories")
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("{id}")
    public Category getCateById(@PathVariable int id) {
        return categoryService.getCateById(id);
    }

    @PostMapping("update")
    public Map<String, String> updateCate(@RequestBody Category category) {
        Map map = new HashMap();
        Category sameNameCate = categoryService.getCateByCateName(category.getName());
        if (category.getId() != sameNameCate.getId()) {
            map.put("message", "existed");
            return map;
        } else {
            Category updatedCate = categoryService.updateCate(category);
            if (updatedCate != null) {
                map.put("message", "success");
                return map;
            } else {
                map.put("message", "fail");
                return map;
            }
        }

        //boolean isExistedCate = categoryService.checkExistCateName(category.getName());
        /*if (isExistedCate) {
            map.put("message", "existed");
            return map;
        } else {
            Category updatedCate = categoryService.updateCate(category);
            if (updatedCate != null) {
                map.put("message", "success");
                return map;
            } else {
                map.put("message", "fail");
                return map;
            }
        }*/
    }

    @PostMapping("create")
    public Map<String, String> createCate(@RequestBody Category category) {
        boolean isExistedCate = categoryService.checkExistCateName(category.getName());
        Map map = new HashMap();
        if (isExistedCate) {
            map.put("message", "existed");
            return map;
        } else {
            Category updatedCate = categoryService.createCate(category);
            if (updatedCate != null) {
                map.put("message", "success");
                return map;
            } else {
                map.put("message", "fail");
                return map;
            }
        }
    }
}
