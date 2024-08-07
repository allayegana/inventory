package com.inventory.small_busness.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "Product")
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String productId;
    private String productName;
    private int quantity;
    private double price;
    private String description;
    private LocalDate date;
    private String category;

    public double getTotalPrice() {
        return this.quantity * this.price;
    }

    public Product(String productName, String productId, int quantity, double unitPrice) {
        this.productName = productName;
        this.productId = productId;
        this.quantity = quantity;
        this.price = unitPrice;
    }
}