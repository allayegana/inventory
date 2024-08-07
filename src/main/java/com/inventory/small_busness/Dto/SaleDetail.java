package com.inventory.small_busness.Dto;

import java.time.LocalDate;


public class SaleDetail {

    private String productName;
    private int quantity;
    private double price;
    private double totalPrice;
    private LocalDate saleDate;
    private String clientName;
    private String clientPhone;
    private String productId;
    private String paymentType;

    public SaleDetail(String productName, int quantity, double price, double totalPrice, LocalDate saleDate,
                      String clientName, String clientPhone, String productId,String paymentType) {
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.totalPrice = totalPrice;
        this.saleDate = saleDate;
        this.clientName = clientName;
        this.clientPhone = clientPhone;
        this.productId = productId;
        this.paymentType = paymentType;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDate getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(LocalDate saleDate) {
        this.saleDate = saleDate;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientPhone() {
        return clientPhone;
    }



    public void setClientPhone(String clientPhone) {
        this.clientPhone = clientPhone;
    }
}
