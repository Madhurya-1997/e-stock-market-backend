package com.heritage.stock.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
    @Min(value = 1, message = "Minimum stock price should be 1")
    @NotNull
    private BigDecimal stockPrice;

    @Field(name = "end_date")
    private Date endDate;

    @Field(name = "stock_duration")
    @NotBlank(message = "Stock duration must be present")
    private Integer stockDuration;

    @Field(name = "company_code")
    private String companyCode;

}
