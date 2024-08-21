package com.locboy.locboybackend.services;


import com.locboy.locboybackend.dtos.CategoryDTO;
import com.locboy.locboybackend.models.Category;
import com.locboy.locboybackend.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryById(long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    @Override
    public Category createCategory(CategoryDTO categoryDTO) {
       Category category = Category.builder()
               .name(categoryDTO.getName())
               .build();

        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(long id, CategoryDTO categoryDTO) {
        Category existingCategory = getCategoryById(id);

        existingCategory.setName(categoryDTO.getName());
        categoryRepository.save(existingCategory);
        return existingCategory;
    }

    @Override
    public void deleteCategory(long id) {
        categoryRepository.deleteById(id);
    }
}
