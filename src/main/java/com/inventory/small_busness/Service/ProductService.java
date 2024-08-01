package com.inventory.small_busness.Service;


import com.inventory.small_busness.Models.Product;
import com.inventory.small_busness.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public Product addOrUpdateProduct(Product newProduct) throws Exception {
        Product existingProduct = productRepository.findByProductId(newProduct.getProductId());
        if (existingProduct != null) {
            // Product with this productId already exists, update the quantity
            existingProduct.setQuantity(existingProduct.getQuantity() + newProduct.getQuantity());
            // Optionally update other fields if needed
            newProduct.setProductName(existingProduct.getProductName());
            newProduct.setPrice(existingProduct.getPrice());
            newProduct.setDescription(existingProduct.getDescription());
            return productRepository.save(existingProduct);
        } else {
            // This is a new product, save it
            return productRepository.save(newProduct);
        }
    }

    public List<Product> findLowStockProducts(int threshold) {
        return productRepository.findByQuantityLessThan(threshold);
    }

    public Product sellProduct(Long id, int quantity) throws Exception {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new Exception("Product not found"));
        if (product.getQuantity() < quantity) {
            throw new Exception("Insufficient stock. you have only " + product.getQuantity() + " " +
                    product.getProductName()  + " in your stock");
        }
        product.setQuantity(product.getQuantity() - quantity);
        return productRepository.save(product);
    }
}


