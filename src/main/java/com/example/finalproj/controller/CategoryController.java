package com.example.finalproj.controller;


import com.example.finalproj.entity.Category;
import com.example.finalproj.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/bakery")
@Controller
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/categories")
    public String categories(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "categories";
    }

    @GetMapping("/addCategories")
    public String addCategories(Model model) {
        model.addAttribute("category", new Category());
        return "addCat";
    }


    @PostMapping("/addCategories")
    public String addCategory(@Valid @ModelAttribute("category") Category category,
                              BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "addCat";
        }

        Category savedCategory = categoryService.addCategory(category);
        if (savedCategory != null) {
            return "redirect:/bakery/categories";
        } else {
            model.addAttribute("errorMessage", "Failed to save category. Try again.");
            return "addCat";
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable("id") Long cat_id) {
          categoryService.deleteCategory(cat_id);
        return ResponseEntity.ok("Deleted");
    }

    @PostMapping("{id}")
    public String updateCategory(@PathVariable("id") long cat_id, @ModelAttribute Category updatedcategory) {
        Category category = categoryService.updateCategory(cat_id, updatedcategory);
        if (category != null) {
            return "redirect:/bakery/categories";
        } else {
            return "error";
        }
    }
}
