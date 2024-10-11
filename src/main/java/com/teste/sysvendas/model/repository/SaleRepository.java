package com.teste.sysvendas.model.repository;

import com.teste.sysvendas.model.entity.Sale;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class SaleRepository {
    private final JdbcTemplate jdbcTemplate;

    public int generateNextId() {
        String sql = "SELECT COALESCE(MAX(id), 0) + 1 FROM sales";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public SaleRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Sale save(Sale sale) {
        String sql = "INSERT INTO sales (client, total_value, quantity) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, sale.getClient(), sale.getTotalValue(), sale.getQuantity());
        return sale;
    }

    public Optional<Sale> findById(int id) {
        String sql = "SELECT * FROM sales WHERE id = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, this::mapRowToSale, id));
    }

    public List<Sale> findAll() {
        String sql = "SELECT * FROM sales";
        return jdbcTemplate.query(sql, this::mapRowToSale);
    }

    public void update(Sale sale) {
        String sql = "UPDATE sales SET client = ?, total_value = ?, quantity = ? WHERE id = ?";
        jdbcTemplate.update(sql, sale.getClient(), sale.getTotalValue(), sale.getQuantity(), sale.getId());
    }

    public void deleteById(int id) {
        String sql = "DELETE FROM sales WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    private Sale mapRowToSale(ResultSet rs, int rowNum) throws SQLException {
        Sale sale = new Sale();
        sale.setId(rs.getInt("id"));
        sale.setClient(rs.getString("client"));
        sale.setTotalValue(rs.getBigDecimal("total_value"));
        sale.setQuantity(rs.getInt("quantity"));
        return sale;
    }

}
