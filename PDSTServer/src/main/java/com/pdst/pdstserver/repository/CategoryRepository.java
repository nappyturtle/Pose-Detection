package com.pdst.pdstserver.repository;

import com.pdst.pdstserver.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Category findCategoryById(int categoryId);

    Category findCategoryByName(String cateName);

    @Query("SELECT COUNT (c.id) FROM Category c")
    int countTotalCategories();

    List<Category> findAllByStatus(String status);
}
