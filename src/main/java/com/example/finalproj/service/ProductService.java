package com.example.finalproj.service;

import com.example.finalproj.entity.Category;
import com.example.finalproj.entity.Product;
import com.example.finalproj.repository.CategoryRepository;
import com.example.finalproj.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Page<Product> getProductsByStatus(String status, Pageable pageable) {
        return productRepository.findByStatus(status, pageable);
    }

    public Page<Product> getProductsByCategory(String categoryName, Pageable pageable) {
        return productRepository.findByCategory_Name(categoryName, pageable);
    }

    public Page<Product> getProductsByStatusAndCategory(String status, String categoryName, Pageable pageable) {
        return productRepository.findByStatusAndCategory_Name(status, categoryName, pageable);
    }

    public Page<Product> searchProducts(String keyword, Pageable pageable) {
        return productRepository.findByProdNameContainingIgnoreCaseOrPrdescriptionContainingIgnoreCase(keyword, keyword, pageable);

    }
    public Product getProductById(int id) {
        Optional<Product> product = productRepository.findById(id);
        return product.orElse(null);
    }

    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    public void deleteProductById(int id) {
        productRepository.deleteById(id);
    }
    public List<Category> getAllCategories() {
    return categoryRepository.findAll();
    }

    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    public Page<Product> searchProducts(String search, String status, String category, Pageable pageable) {
        if ((search == null || search.isEmpty()) && (status == null || status.isEmpty()) && (category == null || category.isEmpty())) {
            return productRepository.findAll(pageable);
        }

        if (status != null && !status.isEmpty() && (category == null || category.isEmpty())) {
            return productRepository.findByStatus(status, pageable);
        }

        if ((status == null || status.isEmpty()) && category != null && !category.isEmpty()) {
            return productRepository.findByCategory_Name(category, pageable);
        }

        if (status != null && !status.isEmpty() && category != null && !category.isEmpty()) {
            return productRepository.findByStatusAndCategory_Name(status, category, pageable);
        }

        return productRepository.findByProdNameContainingIgnoreCaseOrPrdescriptionContainingIgnoreCase(search, search, pageable);
    }
}