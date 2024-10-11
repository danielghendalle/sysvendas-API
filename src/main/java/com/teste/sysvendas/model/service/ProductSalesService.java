package com.teste.sysvendas.model.service;

import com.teste.sysvendas.model.entity.ProductSales;
import com.teste.sysvendas.model.repository.ProductSalesRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductSalesService {
    private final ProductSalesRepository productSalesRepository;

    public ProductSalesService(ProductSalesRepository ProductSalesRepository) {
        this.productSalesRepository = ProductSalesRepository;
    }


    public ProductSales createSale(ProductSales productSales) {
        productSales.setId(productSalesRepository.generateNextId());
        return productSalesRepository.save(productSales);
    }


    public List<ProductSales> getAllProductSales() {
        return productSalesRepository.findAll();
    }

    public Optional<ProductSales> getProductSaleById(int id) {
        return productSalesRepository.findById(id);
    }

    public Optional<ProductSales> getProductSaleBySaleOrProductId(ProductSales productSales) {
        return productSalesRepository.findBySaleIdAndProductId(productSales.getSale().getId(), productSales.getProduct().getId());
    }

    public ProductSales updateProductSales(int id, ProductSales ProductSalesDetails) {
        ProductSales ProductSales = productSalesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venda de produto não encontrada com id: " + id));

        ProductSales.setProduct(ProductSalesDetails.getProduct());
        ProductSales.setSale(ProductSalesDetails.getSale());
        ProductSales.setQuantity(ProductSalesDetails.getQuantity());
        return productSalesRepository.save(ProductSales);
    }

    public void deleteSale(int id) {
        productSalesRepository.deleteById(id);
    }
}
