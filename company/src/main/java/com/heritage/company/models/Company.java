package com.heritage.company.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Document(collection = "company")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Company extends AuditModel{

    @MongoId
    private String id;

    @Field(name = "code")
    @NotNull(message = "Company code cannot be null")
    private String code;

    @Field(name = "name")
    @NotNull(message = "Company name cannot be null")
    private String name ;

    @Field(name = "ceo")
    @NotNull(message = "Company CEO cannot be null")
    private String ceo;

    @Field(name = "turnover")
    @NotNull(message = "Company turnover cannot be null")
    @Min(100000000)
    private Long turnover;

    @Field(name = "website")
    @NotNull(message = "Company website cannot be null")
    private String website;

    @Field(name = "enlisted_stock_exchange")
    @NotNull(message = "Company website cannot be null")
    private String stockExchangeEnlisted;

    @Field(name = "username")
    @NotNull(message = "Username cannot be null")
    private String username;

}


//
