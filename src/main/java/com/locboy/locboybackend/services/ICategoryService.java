package com.locboy.locboybackend.services;

import com.locboy.locboybackend.dtos.CategoryDTO;
import com.locboy.locboybackend.models.Category;

import java.util.List;
import java.util.Optional;

public interface ICategoryService {

    List<Category> getAllCategories();

    Category getCategoryById(long id);

    Category createCategory(CategoryDTO categoryDTO);

    Category updateCategory(long id, CategoryDTO categoryDTO);

    void deleteCategory(long id);
}
