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

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/products")
    public String getProducts(Model model) {
        List<Product> products = productService.findAll();
        model.addAttribute("products", products);
        return "products";
    }

    @GetMapping("/sell")
    public String showSellForm(Model model) {
        List<Product> products = productService.findAll();
        model.addAttribute("products", products);
        return "sell";
    }

    @GetMapping("/product")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "product";
    }

    @PostMapping("/product")
    public String addProduct(Product product, Model model) {
        try {
            productService.addOrUpdateProduct(product);
            return "redirect:/api/v1/inventory/product";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "product";
        }
    }

    @GetMapping("/products/low-stock")
    public String getLowStockProducts(Model model) {
        model.addAttribute("products", productService.findLowStockProducts(5));
        return "low-stock-products";
    }

    @GetMapping("/reports")
    public String showReports(Model model) {
        try {
            List<Product> allProducts = productService.getAllProducts();
            List<Product> lowStockProducts = productService.getLowStockProducts(5);

            model.addAttribute("allProducts", allProducts);
            model.addAttribute("lowStockProducts", lowStockProducts);
            return "reports";
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred while generating the report: " + e.getMessage());
            return "error";
        }
    }

    @PostMapping("/products/sell")
    public String sellProduct(@RequestParam Long productId, @RequestParam int quantity, Model model) {
        try {
            productService.sellProduct(productId, quantity);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            List<Product> products = productService.findAll();
            model.addAttribute("products", products);
            return "sell";
        }
        return "redirect:/api/v1/inventory/products";
    }
}