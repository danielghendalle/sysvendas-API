package com.teste.sysvendas.model.repository;

import com.teste.sysvendas.model.entity.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepository {
    private final JdbcTemplate jdbcTemplate;

    public ProductRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int generateNextId() {
        String sql = "SELECT COALESCE(MAX(id), 0) + 1 FROM products";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public Product save(Product product) {
        String sql = "INSERT INTO products (name, description, quantity, unitary_value) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, product.getName(), product.getDescription(), product.getQuantity(), product.getUnitaryValue());
        return product;
    }

    public Optional<Product> findById(int id) {
        String sql = "SELECT * FROM products WHERE id = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, this::mapRowToProduct, id));
    }

    public List<Product> findAll() {
        String sql = "SELECT * FROM products";
        return jdbcTemplate.query(sql, this::mapRowToProduct);
    }

    public void update(Product product) {
        String sql = "UPDATE products SET name = ?, description = ?, quantity = ?, unitary_value = ? WHERE id = ?";
        jdbcTemplate.update(sql, product.getName(), product.getDescription(), product.getQuantity(), product.getUnitaryValue(), product.getId());
    }

    public void deleteById(int id) {
        String sql = "DELETE FROM products WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    private Product mapRowToProduct(ResultSet rs, int rowNum) throws SQLException {
        Product product = new Product();
        product.setId(rs.getInt("id"));
        product.setName(rs.getString("name"));
        product.setDescription(rs.getString("description"));
        product.setQuantity(rs.getInt("quantity"));
        product.setUnitaryValue(rs.getBigDecimal("unitary_value"));
        return product;
    }
}
