package com.example.finalproj.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product")
public class  Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long prodId;

    @Column(name = "product_name", nullable = false)
    private String prodName;

    @Column(name = "description", nullable = false)
    private String prdescription;

    @Column(name = "status")
    private String status;

    @Column(name = "photo")
    private String photo;

    private double price;

    @Column(name = "exp_date")
    @FutureOrPresent(message = "Date is invalid")
    private LocalDate expDate;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "catId")
    private Category category;

    @Transient
    private MultipartFile photoFile;

    public char[] getName() {
        return prodName.toCharArray();
    }
}
