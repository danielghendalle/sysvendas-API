package com.teste.sysvendas.model.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sale {

    @Id
    private int id;
    private String client;
    private BigDecimal totalValue;
    private Product[] products;
    private int quantity;

}
