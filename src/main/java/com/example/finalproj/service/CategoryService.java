package com.example.finalproj.service;

import com.example.finalproj.entity.Category;
import com.example.finalproj.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category addCategory(Category category) {
        return categoryRepository.save(category);
    }
    public void deleteCategory(Long cat_id) {
        Category category = categoryRepository.findById(cat_id).orElseThrow(() -> new RuntimeException("Category not found"));
        categoryRepository.deleteById(cat_id);
    }

    public Category updateCategory(Long cat_id, Category updatedCategory) {
        Category category = categoryRepository.findById(cat_id).orElseThrow(() -> new RuntimeException("Category not found"));

        category.setName(updatedCategory.getName());
        category.setDescription(updatedCategory.getDescription());

        Category updatedCateO = categoryRepository.save(category);
        return updatedCateO;
    }

    public Category getCategoryById(Long catId) {
        return categoryRepository.findById(catId).orElseThrow(() -> new RuntimeException("Category not found"));
    }
}
