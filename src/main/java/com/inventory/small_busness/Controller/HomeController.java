package com.inventory.small_busness.Controller;


import com.inventory.small_busness.Models.Product;
import com.inventory.small_busness.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/inventory")
public class HomeController {

    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public String getProducts(Model model) {
        List<Product> products = productService.findAll();
        model.addAttribute("products", products);
        model.addAttribute("product", new Product());
        return "products";
    }

    @PostMapping("/products")
    public String addProduct(@ModelAttribute Product product) {
        productService.save(product);
        return "redirect:/api/v1/inventory/products";
    }

    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        return "redirect:/api/v1/inventory/products";
    }

    @GetMapping("/products/low-stock")
    public String getLowStockProducts(Model model) {
        model.addAttribute("products", productService.findLowStockProducts(5));
        return "low-stock-products";
    }

    @PostMapping("/products/sell")
    public String sellProduct(@RequestParam Long id, @RequestParam int quantity, Model model) {
        try {
            productService.sellProduct(id, quantity);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/api/v1/inventory/products";
    }
}
