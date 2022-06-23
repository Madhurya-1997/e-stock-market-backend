package com.heritage.stock.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.math.BigDecimal;
import java.util.Date;


@Document(collection = "stock")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stock extends AuditModel{

    @MongoId
    private String id;

    @Field(name = "stock_price")
    private BigDecimal stockPrice;

    @Field(name = "end_date")
    private Date endDate;

    @Field(name = "stock_duration")
    private Integer stockDuration;

    @Field(name = "company_code")
    private String companyCode;

}
