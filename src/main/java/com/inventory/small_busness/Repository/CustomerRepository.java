package com.inventory.small_busness.Repository;

import com.inventory.small_busness.Dto.SaleDetail;
import com.inventory.small_busness.Models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<SaleDetail> findBySaleDate(LocalDate today);
    List<Customer> findByProductIdAndSaleDateBetween(String productId, LocalDate dateStart, LocalDate dateEnd);


    List<Customer> findByProductId(String productId);

    List<Customer> findBySaleDateBetween(LocalDate dateStart, LocalDate dateEnd);
}
