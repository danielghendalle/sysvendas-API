package com.teste.sysvendas.model.repository;

import com.teste.sysvendas.model.entity.Product;
import com.teste.sysvendas.model.entity.ProductSales;
import com.teste.sysvendas.model.entity.Sale;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class ProductSalesRepository {
    private final JdbcTemplate jdbcTemplate;

    public ProductSalesRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int generateNextId() {
        String sql = "SELECT COALESCE(MAX(id), 0) + 1 FROM product_sales";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public ProductSales save(ProductSales productSales) {
        String sql = "INSERT INTO product_sales (sale_id, product_id, quantity) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, productSales.getSale().getId(), productSales.getProduct().getId(), productSales.getQuantity());
        return productSales;
    }

    public Optional<ProductSales> findById(int id) {
        String sql = "SELECT * FROM product_sales WHERE sale_id = ? AND product_id = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, this::mapRowToProductSales, id));
    }

    public Optional<ProductSales> findBySaleIdAndProductId(int saleId, int productId) {
        String sql = "SELECT * FROM product_sales WHERE sale_id = ? AND product_id = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, this::mapRowToProductSales, saleId, productId));
    }

    public List<ProductSales> findAll() {
        String sql = "SELECT * FROM product_sales";
        return jdbcTemplate.query(sql, this::mapRowToProductSales);
    }

    public void deleteById(int id) {
        String sql = "DELETE FROM product_sales WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    private ProductSales mapRowToProductSales(ResultSet rs, int rowNum) throws SQLException {
        ProductSales productSales = new ProductSales();
        productSales.setId(rs.getInt("id"));

        Sale sale = new Sale();
        sale.setId(rs.getInt("sale_id"));
        productSales.setSale(sale);

        Product product = new Product();
        product.setId(rs.getInt("product_id"));
        productSales.setProduct(product);

        productSales.setQuantity(rs.getInt("quantity"));

        return productSales;
    }
}
