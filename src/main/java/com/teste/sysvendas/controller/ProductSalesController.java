package com.teste.sysvendas.controller;

import com.teste.sysvendas.model.entity.ProductSales;
import com.teste.sysvendas.model.service.ProductSalesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ProductSalesController.PATH)
public class ProductSalesController {

    public static final String PATH = "/product-sales";

    private final ProductSalesService productSalesService;

    public ProductSalesController(ProductSalesService productSalesService) {
        this.productSalesService = productSalesService;
    }

    @PostMapping
    public ProductSales createProductSales(@RequestBody ProductSales productSales) {
        return productSalesService.createSale(productSales);
    }

    @GetMapping
    public List<ProductSales> getAllProductSales() {
        return productSalesService.getAllProductSales();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductSales> getProductSalesById(@PathVariable int id) {
        return productSalesService.getProductSaleById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductSales> updateProductSales(@PathVariable int id, @RequestBody ProductSales productSalesDetails) {
        ProductSales updatedProductSales = productSalesService.updateProductSales(id, productSalesDetails);
        return ResponseEntity.ok(updatedProductSales);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductSales(@PathVariable int id) {
        productSalesService.deleteSale(id);
        return ResponseEntity.noContent().build();
    }
}
