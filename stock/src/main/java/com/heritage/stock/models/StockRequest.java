package com.heritage.stock.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockRequest {

    private BigDecimal price;
    private Integer duration;
    private String companyCode;
}
