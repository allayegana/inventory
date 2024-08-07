package com.inventory.small_busness.Models;


import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Data
public class Customer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String productId;
    private String productName;
    private int quantity;
    private double price;
    private double totalPrice;
    private String clientName;
    private String clientPhone;
    private String paymentType;
    private LocalDate saleDate;
}
