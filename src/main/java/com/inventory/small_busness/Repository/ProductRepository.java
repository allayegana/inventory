package com.inventory.small_busness.Repository;

import com.inventory.small_busness.Models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByQuantityLessThan(int quantity);

    List<Product> findByProductId(String productId);
    Product findByCategory(String category);

    List<Product> findBySaleDate(LocalDate today);

    List<Product> findByProductIdAndDateBetween(String productId, LocalDate dateStart, LocalDate dateEnd);

    List<Product> findByDateBetween(LocalDate dateStart, LocalDate dateEnd);
}
