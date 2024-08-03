package com.inventory.small_busness.Service;


import com.inventory.small_busness.Models.Product;
import com.inventory.small_busness.Repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> findAll() {
        return productRepository.findAll();
    }


    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getLowStockProducts(int threshold) {
        return productRepository.findByQuantityLessThan(threshold);
    }

    public Product findById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public void save(Product product) {
        productRepository.save(product);
    }

    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }


    public Product addOrUpdateProduct(Product newProduct) throws Exception {
        Product existingProduct = productRepository.findByCategory(newProduct.getCategory());

        if (existingProduct != null && newProduct.getProductName().equalsIgnoreCase(existingProduct.getProductName())) {
            // Product with this category and name already exists, update the quantity
            existingProduct.setQuantity(existingProduct.getQuantity() + newProduct.getQuantity());
            existingProduct.setSaleDate(LocalDate.now());
            return productRepository.save(existingProduct);
        } else {
            // This is a new product, generate a random 10-digit productId
            newProduct.setProductId(generateRandomProductId());
            newProduct.setSaleDate(LocalDate.now());
            return productRepository.save(newProduct);
        }
    }

    private String generateRandomProductId() {
        Random random = new Random();
        StringBuilder productId = new StringBuilder(10);

        for (int i = 0; i < 10; i++) {
            productId.append(random.nextInt(10)); // Generates a random digit (0-9)
        }

        return productId.toString();
    }

    public List<Product> getDailySales() {
        LocalDate today = LocalDate.now();
        List<Product> dailySales = productRepository.findBySaleDate(today);

        // Calculate total price for each product
        for (Product product : dailySales) {
            product.setTotalPrice(product.getQuantity() * product.getPrice());
        }

        return dailySales;
    }

    // Calculate grand total

    public double getGrandTotal(List<Product> dailySales) {
        return dailySales.stream()
                .mapToDouble(Product::getTotalPrice)
                .sum();
    }

    public List<Product> searchProducts(String productId, LocalDate dateStart, LocalDate dateEnd) {
        if (productId != null && !productId.isEmpty() && dateStart != null) {
            return productRepository.findByProductIdAndDateBetween(productId, dateStart, dateEnd);
        } else if (productId != null && !productId.isEmpty()) {
            return productRepository.findByProductId(productId);
        } else {
            return productRepository.findByDateBetween(dateStart, dateEnd);
        }
    }

    public double calculateTotalUnitPrice(List<Product> products) {
        return products.stream()
                .mapToDouble(Product::getPrice)
                .sum();
    }

    public double calculateGrandTotal(List<Product> products) {
        return products.stream()
                .mapToDouble(Product::getTotalPrice)
                .sum();
    }

    public List<Product> findLowStockProducts(int threshold) {
        return productRepository.findByQuantityLessThan(threshold);
    }

    public Product sellProduct(Long id, int quantity) throws Exception {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new Exception("Product not found"));
        if (product.getQuantity() < quantity) {
            throw new Exception("Insufficient stock. you have only " + product.getQuantity() + " " +
                    product.getProductName() + " in your stock");
        }
        product.setQuantity(product.getQuantity() - quantity);
        product.setDate(LocalDate.now());
        return productRepository.save(product);
    }

    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + productId));
    }

    public void updateProduct(Product product) {
        productRepository.save(product);
    }
}


