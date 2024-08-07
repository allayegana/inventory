package com.inventory.small_busness.Service;


import com.inventory.small_busness.Dto.SaleDetail;
import com.inventory.small_busness.Models.Customer;
import com.inventory.small_busness.Models.Product;
import com.inventory.small_busness.Repository.CustomerRepository;
import com.inventory.small_busness.Repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public List<Product> findAll() {
        return productRepository.findAll();
    }


    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getLowStockProducts(int threshold) {
        return productRepository.findByQuantityLessThan(threshold);
    }



    public void save(Product product) {
        productRepository.save(product);
    }

    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }


    public Product addOrUpdateProduct(Product newProduct) throws Exception {
        newProduct.setDate(LocalDate.now());

        // Check for existing product with the same name and category
        Product existingProduct = productRepository.findByCategoryAndProductName(newProduct.getCategory(), newProduct.getProductName());

        if (existingProduct != null) {
            // Product exists, update the quantity
            existingProduct.setQuantity(existingProduct.getQuantity() + newProduct.getQuantity());
            return productRepository.save(existingProduct);
        } else {
            // This is a new product, assign a new ID and save it
            newProduct.setProductId(generateRandomProductId());
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

    public List<SaleDetail> getDailySalesDetails() {
        LocalDate today = LocalDate.now();
        return customerRepository.findBySaleDate(today); // Implement this method in your repository
    }

    public double getGrandTotal(List<SaleDetail> dailySales) {
        return dailySales.stream()
                .mapToDouble(SaleDetail::getTotalPrice)
                .sum();
    }


    public List<Customer> searchProducts(String productId, LocalDate dateStart, LocalDate dateEnd) {
        if (productId != null && !productId.isEmpty() && dateStart != null) {
            return customerRepository.findByProductIdAndSaleDateBetween(productId, dateStart, dateEnd);
        } else if (productId != null && !productId.isEmpty()) {
            return customerRepository.findByProductId(productId);
        } else {
            return customerRepository.findBySaleDateBetween(dateStart, dateEnd);
        }
    }

    public double calculateTotalUnitPrice(List<Customer> products) {
        return products.stream()
                .mapToDouble(Customer::getPrice)
                .sum();
    }

    public double calculateGrandTotal(List<Customer> products) {
        return products.stream()
                .mapToDouble(Customer::getTotalPrice)
                .sum();
    }

    public List<Product> findLowStockProducts(int threshold) {
        return productRepository.findByQuantityLessThan(threshold);
    }

    public Product sellProduct(Long id, int quantity, String clientName, String clientPhone, String paymentType) throws Exception {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new Exception("Product not found"));

        if (product.getQuantity() < quantity) {
            throw new Exception("Insufficient stock. You have only " + product.getQuantity() + " " +
                    product.getProductName() + " in your stock.");
        }

        product.setQuantity(product.getQuantity() - quantity);
        product.setDate(LocalDate.now());
        Product updatedProduct = productRepository.save(product);

        // Create a new sale record
        Customer sale = new Customer();
        sale.setProductId(product.getProductId());
        sale.setProductName(product.getProductName());
        sale.setQuantity(quantity);
        sale.setPrice(product.getPrice());
        sale.setTotalPrice(product.getPrice() * quantity);
        sale.setClientName(clientName);
        sale.setClientPhone(clientPhone);
        sale.setPaymentType(paymentType);
        sale.setSaleDate(LocalDate.now());

        // Save the sale record to the database
        customerRepository.save(sale);

        return updatedProduct;
    }
    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + productId));
    }

    public void updateProduct(Product product) {
        Product existP = productRepository.findById(product.getId()).orElse(null);
        if (product.getId() == null) {
            throw new IllegalArgumentException("Product ID must not be null");
        }
        assert existP != null;
        existP.setProductName(product.getProductName());
           product.setProductId(existP.getProductId());
            existP.setQuantity(product.getQuantity());
            existP.setPrice(product.getPrice());
            existP.setDate(LocalDate.now());
            existP.setDescription(product.getDescription());
            productRepository.save(existP);

    }

    public Product findById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public List<SaleDetail> getRecentSalesDetails() {
        LocalDate today = LocalDate.now();
        List<SaleDetail> sales = customerRepository.findBySaleDate(today); // Fetch sales from today

        // Transform Sales into SaleDetails
        return sales.stream()
                .map(sale -> new SaleDetail(

                        sale.getProductName(),
                        sale.getQuantity(),
                        sale.getPrice(),
                        sale.getTotalPrice(),
                        sale.getSaleDate(),
                        sale.getClientName(),
                        sale.getClientPhone(),
                        sale.getProductId(),
                        sale.getPaymentType()
                ))
                .collect(Collectors.toList());
    }
}



