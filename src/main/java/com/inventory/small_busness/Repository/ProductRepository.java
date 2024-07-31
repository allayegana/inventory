package com.inventory.small_busness.Repository;

import com.inventory.small_busness.Models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByQuantityLessThan(int quantity);
}
