package com.example.finalproj.repository;

import com.example.finalproj.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    Page<Product> findByStatus(String status, Pageable pageable);

    Page<Product> findByCategory_Name(String categoryName, Pageable pageable);

    Page<Product> findByStatusAndCategory_Name(String status, String categoryName, Pageable pageable);

    Page<Product> findByProdNameContainingIgnoreCaseOrPrdescriptionContainingIgnoreCase(String prodNameKeyword, String descriptionKeyword, Pageable pageable);

}