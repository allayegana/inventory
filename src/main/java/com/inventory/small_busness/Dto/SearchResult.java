package com.inventory.small_busness.Dto;

import com.inventory.small_busness.Models.Product;

import java.util.List;

public class SearchResult {
    private List<Product> products;
    private double totalUnitPrice;
    private double grandTotal;

    // Constructors, getters, and setters

    public SearchResult(List<Product> products, double totalUnitPrice, double grandTotal) {
        this.products = products;
        this.totalUnitPrice = totalUnitPrice;
        this.grandTotal = grandTotal;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public double getTotalUnitPrice() {
        return totalUnitPrice;
    }

    public void setTotalUnitPrice(double totalUnitPrice) {
        this.totalUnitPrice = totalUnitPrice;
    }

    public double getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(double grandTotal) {
        this.grandTotal = grandTotal;
    }
}

