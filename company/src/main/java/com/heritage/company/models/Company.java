package com.heritage.company.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Document(collection = "company")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Company extends AuditModel{

    @MongoId
    private String id;

    @Field(name = "code")
    @NotBlank(message = "Company code should be present")
    private String code;

    @Field(name = "name")
    @NotBlank(message = "Company name should be present")
    private String name ;

    @Field(name = "ceo")
    @NotBlank(message = "Company CEO should be present")
    private String ceo;

    @Field(name = "turnover")
    @NotNull(message = "Company turnover cannot be empty")
    @Min(value = 100000000, message = "Company turnover must be greater than or equal to INR 10cr.")
    private Long turnover;

    @Field(name = "website")
    @NotBlank(message = "Company website should be present")
    private String website;

    @Field(name = "enlisted_stock_exchange")
    @NotBlank(message = "Stock exchange value should be present")
    private String stockExchangeEnlisted;
}
