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

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    public List<Product> findLowStockProducts(int threshold) {
        return productRepository.findByQuantityLessThan(threshold);
    }

    public Product sellProduct(Long id, int quantity) throws Exception {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new Exception("Produit non trouvé"));
        if (product.getQuantity() < quantity) {
            throw new Exception("Stock insuffisant");
        }
        product.setQuantity(product.getQuantity() - quantity);
        return productRepository.save(product);
    }
}


