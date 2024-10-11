package com.teste.sysvendas.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSales {

    @Id
    private int id;
    private Sale sale;
    private Product product;
    private int quantity;
}
