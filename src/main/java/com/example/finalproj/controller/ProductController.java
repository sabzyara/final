package com.example.finalproj.controller;
import com.example.finalproj.entity.Category;
import com.example.finalproj.entity.Product;
import com.example.finalproj.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import java.util.UUID;

@RequestMapping("/bakery")
@Controller
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final String UPLOAD_DIR = "C:\\Users\\whyco\\IdeaProjects\\finalproj\\src\\main\\resources\\static\\uploads\\";

    @Autowired
    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping("/products")
    public String getAllProducts(@RequestParam(defaultValue ="0") int page,
                                 @RequestParam(defaultValue = "8") int size,
                                 @RequestParam(required = false) String search,
                                 @RequestParam(required = false) String status,
                                 @RequestParam(required = false) String category,
                                 Model model) {
        List<Category> categories = productService.getAllCategories();
        model.addAttribute("categories", categories);
        Page<Product> productPage;
        if (search != null && !search.trim().isEmpty() || status != null && category != null) {
            productPage = productService.searchProducts(search ,status , category , PageRequest.of(page, size));
        } else {
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
                              @RequestParam MultipartFile photoFile,
                              BindingResult bindingResult,
                              Model model) {

        try {
            String fileName = photoFile.getOriginalFilename();
            Path path = Paths.get(UPLOAD_DIR + fileName);
            Files.createDirectories(path.getParent());
            Files.write(path, photoFile.getBytes());

            product.setPhoto(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("category", categoryService.getAllCategories());
            return "new_product";
        }

        if (product.getCategory() != null) {
            Long categoryId = product.getCategory().getCatId();
            Category managedCategory = categoryService.getCategoryById(categoryId);
            product.setCategory(managedCategory);
        }

        productService.addProduct(product);

        return "redirect:/bakery/products";
    }

    @GetMapping("/updateProduct/{id}")
    public String showFormForUpdate(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        if (product == null) {
            model.addAttribute("error", "Product not found.");
            return "redirect:/bakery/products";
        }

        model.addAttribute("product", product);
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "updateProduct";
    }
    @PostMapping("/updateProduct/{id}")
    public String updateProduct(@PathVariable Long id,
                                @Valid @ModelAttribute("product") Product product,
                                BindingResult result,
                                @RequestParam(value = "image", required = false) MultipartFile image,
                                Model model) {
        if (result.hasErrors()) {
            return "updateProduct";
        }

        Product existingProduct = productService.getProductById(id);
        if (existingProduct == null) {
            model.addAttribute("error", "Product not found.");
            return "redirect:/bakery/products";
        }


        existingProduct.setProdName(product.getProdName());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setStatus(product.getStatus());


        if (product.getCategory() != null && product.getCategory().getCatId() != null) {
            Category category = categoryService.getCategoryById(product.getCategory().getCatId());
            if (category != null) {
                existingProduct.setCategory(category);
            } else {
                model.addAttribute("error", "Invalid category selected.");
                return "updateProduct";
            }
        }

        if (product.getPrdescription() != null && !product.getPrdescription().isEmpty()) {
            existingProduct.setPrdescription(product.getPrdescription());
        }

        if (image != null && !image.isEmpty()) {
            try {
                String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
                Path uploadPath = Paths.get(UPLOAD_DIR);

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Path filePath = uploadPath.resolve(fileName);
                Files.write(filePath, image.getBytes());
                existingProduct.setPhoto(fileName);
            } catch (IOException e) {
                model.addAttribute("error", "Failed to upload image.");
                return "updateProduct";
            }
        }

        productService.saveProduct(existingProduct);

        model.addAttribute("success", "Product updated successfully!");
        return "redirect:/bakery/products";
    }


    @GetMapping("/deleteProduct/{id}")
    public String deleteProduct(@PathVariable("id") long id) {
        productService.deleteProductById(id);
        return "redirect:/bakery/products";
    }

    @GetMapping("/productsUser")
    public String getAllProductsUser(@RequestParam(defaultValue ="0") int page,
                                     @RequestParam(defaultValue = "8") int size,
                                     @RequestParam(required = false) String search,
                                     @RequestParam(required = false) String status,
                                     @RequestParam(required = false) String category,
                                     Model model) {
        List<Category> categories = productService.getAllCategories();
        model.addAttribute("categories", categories);
        Page<Product> productPage;
        if (search != null && !search.trim().isEmpty() || status != null && category != null) {
            productPage = productService.searchProducts(search ,status , category , PageRequest.of(page, size));
        } else {
            productPage = productService.getAllProducts(PageRequest.of(page, size));
        }
        model.addAttribute("product", productPage.getContent());
        model.addAttribute("current", productPage.getNumber());
        model.addAttribute("total", productPage.getTotalPages());
        model.addAttribute("search", search == null ? "" : search);
        model.addAttribute("status", status );
        model.addAttribute("category", category);

        return "productsUser";
    }
}
