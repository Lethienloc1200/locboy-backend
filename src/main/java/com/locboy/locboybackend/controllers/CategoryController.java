package com.locboy.locboybackend.controllers;


import com.locboy.locboybackend.dtos.CategoryDTO;
import com.locboy.locboybackend.models.Category;
import com.locboy.locboybackend.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.engine.jdbc.mutation.spi.BindingGroup;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("")
    public ResponseEntity<?> createCategory(
            @Valid @RequestBody CategoryDTO categoryDTO,
            BindingResult result) {

        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorMessages);
        }
        categoryService.createCategory(categoryDTO);
        return ResponseEntity.ok("Insert category successfully");
    }

    @GetMapping("")
    public ResponseEntity<List<Category>> getAllCategories(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateCatergory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryDTO categoryDTO, BindingResult result
    ){
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorMessages);
        }
        categoryService.updateCategory(id,categoryDTO);
        return ResponseEntity.ok("Update successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id){
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Delete category with:" + id + "sucessfully");
    }
}
