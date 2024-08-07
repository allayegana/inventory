package com.inventory.small_busness.Controller;

import com.inventory.small_busness.Dto.SaleDetail;
import com.inventory.small_busness.Dto.SearchResult;
import com.inventory.small_busness.Models.Customer;
import com.inventory.small_busness.Models.Product;
import com.inventory.small_busness.Service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.ArrayList;
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

    @GetMapping("/receipt")
    public String receipt(Model model) {
        return "receipt";
    }

    @GetMapping("/access-denied")
    public String accessDenied(Model model) {
        return "access-denied";
    }

    @GetMapping("/products")
    public String getProducts(Model model) {
        List<Product> products = productService.findAll();
        model.addAttribute("products", products);
        return "products";
    }


    @GetMapping("/daily-sales-search")
    public String getDailySales(@RequestParam(required = false) String productId,
                                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateStart,
                                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateEnd,
                                Model model) {
        List<Customer> dailySales = productService.searchProducts(productId, dateStart, dateEnd);
        double totalUnitPrice = productService.calculateTotalUnitPrice(dailySales);
        double grandTotal = productService.calculateGrandTotal(dailySales);

        model.addAttribute("dailySales", dailySales);
        model.addAttribute("totalUnitPrice", totalUnitPrice);
        model.addAttribute("grandTotal", grandTotal);

        return "daily-sales";
    }


    @GetMapping("/daily-sales")
    public String getDailySales(Model model) {
        List<SaleDetail> dailySales = productService.getDailySalesDetails(); // Ensure this matches your service layer
        double grandTotal = productService.getGrandTotal(dailySales); // This needs to be adjusted if SaleDetail is used

        model.addAttribute("dailySales", dailySales);
        model.addAttribute("grandTotal", grandTotal);

        return "daily-sales";
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
            return "redirect:/api/v1/inventory/products";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to add or update the product: " + e.getMessage());
            e.printStackTrace();  // For backend debugging
            return "product";
        }
    }


    @GetMapping("/receipt-sale")
    public String showReceipt(Model model) {
        List<SaleDetail> productSales = productService.getRecentSalesDetails();
        double grandTotal = productService.getGrandTotal(productSales);

        model.addAttribute("productSales", productSales);
        model.addAttribute("grandTotal", grandTotal);

        return "receipt"; // Thymeleaf template name
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


    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteById(id);
        return "redirect:/api/v1/inventory/reports";
    }


    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Product product = productService.findById(id);
        if (product != null) {
            model.addAttribute("product", product);
            return "editProduct"; // Assuming "editProduct" is your Thymeleaf template name
        } else {
            return "redirect:/api/v1/inventory/reports"; // Redirect if product is not found
        }
    }

    // Method to handle the form submission
    @PostMapping("/edit")
    public String editProduct(@ModelAttribute("product") Product product) {
        productService.updateProduct(product);

        return "redirect:/api/v1/inventory/reports";
    }



    @PostMapping("/sell")
    public String sellProducts(@RequestParam("productId") List<Long> productIds,
                               @RequestParam("quantity") List<Integer> quantities,
                               @RequestParam("clientName") String clientName,
                               @RequestParam("clientPhone") String clientPhone,
                               @RequestParam("paymentType") String paymentType,
                               RedirectAttributes redirectAttributes) {
        try {
            List<Product> productSales = new ArrayList<>();
            double grandTotal = 0;

            for (int i = 0; i < productIds.size(); i++) {
                Long id = productIds.get(i);
                int quantity = quantities.get(i);

                Product updatedProduct = productService.sellProduct(id, quantity, clientName, clientPhone, paymentType);
                double totalPrice = updatedProduct.getPrice() * quantity;

                productSales.add(new Product(updatedProduct.getProductName(), updatedProduct.getProductId(), quantity, updatedProduct.getPrice()));
                grandTotal += totalPrice;
            }

            // Add attributes for the receipt page
            redirectAttributes.addFlashAttribute("productSales", productSales);
            redirectAttributes.addFlashAttribute("grandTotal", grandTotal);
            redirectAttributes.addFlashAttribute("clientName", clientName);
            redirectAttributes.addFlashAttribute("clientPhone", clientPhone);
            redirectAttributes.addFlashAttribute("paymentType", paymentType);

            return "redirect:/api/v1/inventory/receipt";
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", "Product not found");
            return "redirect:/api/v1/inventory/sell";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/api/v1/inventory/sell";
        }
    }

}