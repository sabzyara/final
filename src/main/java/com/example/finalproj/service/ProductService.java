package com.example.finalproj.service;

import com.example.finalproj.entity.Category;
import com.example.finalproj.entity.Product;
import com.example.finalproj.repository.CategoryRepository;
import com.example.finalproj.repository.ProductRepository;
import jakarta.transaction.Transactional;
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


    public Product getProductById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        return product.orElse(null);
    }

    public void deleteProductById(Long id) {
        if (!productRepository.existsById(id)) {
            throw new IllegalArgumentException("Product with ID " + id + " does not exist");
        }
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

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Transactional
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }


}