package com.teste.sysvendas.model.service;

import com.teste.sysvendas.model.entity.Product;
import com.teste.sysvendas.model.entity.ProductSales;
import com.teste.sysvendas.model.entity.Sale;
import com.teste.sysvendas.model.repository.ProductRepository;
import com.teste.sysvendas.model.repository.ProductSalesRepository;
import com.teste.sysvendas.model.repository.SaleRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class SaleService {
    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;
    private final ProductSalesRepository productSalesRepository;

    public SaleService(SaleRepository saleRepository, ProductRepository productRepository, ProductSalesRepository productSalesRepository) {
        this.saleRepository = saleRepository;
        this.productRepository = productRepository;
        this.productSalesRepository = productSalesRepository;
    }


    public Sale createSale(Sale sale) {
        if (sale.getProducts() == null || sale.getProducts().length == 0) {
            throw new IllegalArgumentException("A venda deve conter pelo menos um produto.");
        }

        BigDecimal totalValue = BigDecimal.ZERO;

        for (Product product : sale.getProducts()) {
            Product productFromDb = productRepository.findById(product.getId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado com id: " + product.getId()));

            if (product.getQuantity() <= 0) {
                throw new IllegalArgumentException("A quantidade de cada produto deve ser maior que zero.");
            }

            if (product.getQuantity() > productFromDb.getQuantity()) {
                throw new IllegalArgumentException("Estoque insuficiente para o produto: " + productFromDb.getName());
            }

            totalValue = totalValue.add(productFromDb.getUnitaryValue().multiply(BigDecimal.valueOf(sale.getQuantity())));
        }


        Sale savedSale = null;
        for (Product product : sale.getProducts()) {
            Product productFromDb = productRepository.findById(product.getId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado com id: " + product.getId()));

            productFromDb.setQuantity(productFromDb.getQuantity() - sale.getQuantity());

            sale.setId(saleRepository.generateNextId());
            savedSale = saleRepository.save(sale);

            ProductSales productSales = new ProductSales();
            productSales.setSale(savedSale);
            productSales.setProduct(productFromDb);
            productSales.setQuantity(product.getQuantity());
            productSalesRepository.save(productSales);
            productRepository.update(productFromDb);
        }


        return savedSale;
    }

    public List<Sale> getAllSales() {
        return saleRepository.findAll();
    }

    public Optional<Sale> getSaleById(int id) {
        return saleRepository.findById(id);
    }

    public Sale updateSale(int id, Sale SaleDetails) {
        Sale Sale = saleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venda não encontrada com id: " + id));

        Sale.setClient(SaleDetails.getClient());
        Sale.setTotalValue(SaleDetails.getTotalValue());
        Sale.setProducts(SaleDetails.getProducts());
        Sale.setQuantity(SaleDetails.getQuantity());
        return saleRepository.save(Sale);
    }

    public void deleteSale(int id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venda não encontrada com id: " + id));

        for (Product product : sale.getProducts()) {

            Product productFromDb = productRepository.findById(product.getId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado com id: " + product.getId()));

            ProductSales productSales = productSalesRepository.findBySaleIdAndProductId(sale.getId(), product.getId())
                    .orElseThrow(() -> new RuntimeException("Relação de produto-venda não encontrada."));

            productFromDb.setQuantity(productFromDb.getQuantity() + productSales.getQuantity());

            productRepository.save(productFromDb);

            productSalesRepository.deleteById(productSales.getId());
        }

        saleRepository.deleteById(sale.getId());
    }
}