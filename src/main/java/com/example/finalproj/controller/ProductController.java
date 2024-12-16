package com.example.finalproj.controller;

import com.example.finalproj.entity.Category;
import com.example.finalproj.entity.Product;
import com.example.finalproj.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import com.example.finalproj.service.CategoryService;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RequestMapping("/bakery")
@Controller
public class ProductController {
    private final ProductService productService;

    private final CategoryService categoryService;

    @Autowired
    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

//    @GetMapping("/products")
//    public String getAllProducts(@RequestParam(required = false) String status,
//                                 @RequestParam(required = false) String category,
//                                 @RequestParam(required = false, defaultValue = "0") int page,
//                                 @RequestParam(required = false, defaultValue = "10") int size,
//                                 @RequestParam(required = false) String keyword,
//                                 Model model) {
//        Pageable pageable = PageRequest.of(page, size);
//        Page<Product> products;
//
//        if (status != null && !status.isEmpty() && category != null && !category.isEmpty()) {
//            products = productService.getProductsByStatusAndCategory(status, category, pageable);
//        } else if (status != null && !status.isEmpty()) {
//            products = productService.getProductsByStatus(status, pageable);
//        } else if (category != null && !category.isEmpty()) {
//            products = productService.getProductsByCategory(category, pageable);
//        } else if (keyword != null && !keyword.isEmpty()) {
//            products = productService.searchProducts(keyword, pageable);
//        } else {
//            products = productService.getAllProducts(pageable);
//        }
//
//        model.addAttribute("listProducts", products);
//        model.addAttribute("currentPage", page);
//        model.addAttribute("totalPages", products.getTotalPages());
//        model.addAttribute("status", status);
//        model.addAttribute("category", category);
//        return "products";
//    }

    @GetMapping("/products")
    public String getAllProducts(@RequestParam(defaultValue ="0") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 @RequestParam(required = false) String search,
                                 @RequestParam(required = false) String status,
                                 @RequestParam(required = false) String category,
                                 Model model) {
        List<Category> categories = productService.getAllCategories();
        model.addAttribute("categories", categories);
        Page<Product> productPage;
        if (search != null && !search.trim().isEmpty() || status != null && category != null) {
            productPage = productService.searchProducts(search ,status , category , PageRequest.of(page, size));
        }else {
            productPage = productService.getAllProducts(PageRequest.of(page, size));
        }
        model.addAttribute("product", productPage.getContent());
        model.addAttribute("current", productPage.getNumber());
        model.addAttribute("total", productPage.getTotalPages());
        model.addAttribute("search", search == null ? "" : search);
        model.addAttribute("status", status );
        model.addAttribute("category", category);

        return "products";
    }

//    @GetMapping("/saveProduct")
//    public String showNewProductForm(Model model) {
//        Product product = new Product();
//        model.addAttribute("product", product);
//        List<Category> categories = productService.getAllCategories();
//        model.addAttribute("categories", categories);
//        return "new_product";
//    }

//    @PostMapping("/saveProduct")
//    public String saveProduct(@Valid @ModelAttribute("product") Product product, BindingResult result, Model model) {
//        if (result.hasErrors()) {
//            model.addAttribute("categories", productService.getAllCategories());
//            return "new_product";
//        }
//
//        if (product.getPhotoFile() != null && !product.getPhotoFile().isEmpty()) {
//            String photoUrl = uploadPhoto(product.getPhotoFile());
//            product.setPhoto(photoUrl);
//        }
//
//        productService.saveProduct(product);
//        return "redirect:/bakery/products";
//    }


    @GetMapping("/saveProduct")
    public String showNewProductForm(Model model) {
        Product product = new Product();
        model.addAttribute("product", product);

        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("category", categories);

        return "new_product";
    }

    @PostMapping("/saveProduct")
    public String saveProduct(@Valid @ModelAttribute("product") Product product,
                              BindingResult bindingResult,
                              Model model) {
        // Handle validation errors
        if (bindingResult.hasErrors()) {
            // Re-add categories to the model, since they won't persist across the request
            model.addAttribute("category", categoryService.getAllCategories());
            return "new_product"; // Return the form view
        }

        // Process the uploaded photo file (if applicable)
        if (product.getPhotoFile() != null && !product.getPhotoFile().isEmpty()) {
            String photoUrl = uploadPhoto(product.getPhotoFile());
            product.setPhoto(photoUrl);
        }

        // Save the product
        productService.addProduct(product);

        return "redirect:/products"; // Redirect to the product list
    }


    @GetMapping("/showFormForUpdateProduct/{id}")
    public String showFormForUpdate(@PathVariable int id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        List<Category> categories = productService.getAllCategories();
        model.addAttribute("categories", categories);
        return "update_product";
    }

    @GetMapping("/deleteProduct/{id}")
    public String deleteProduct(@PathVariable("id") int id) {
        productService.deleteProductById(id);
        return "redirect:/products";
    }

    private String uploadPhoto(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        Path filePath = Paths.get("images/" + fileName);
        try {
            Files.copy(file.getInputStream(), filePath);
            return "/images/" + fileName;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}