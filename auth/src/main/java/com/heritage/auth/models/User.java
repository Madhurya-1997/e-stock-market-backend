package com.heritage.auth.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Document(collection = "user")
@Data
@NoArgsConstructor
public class User extends AuditModel {

    @MongoId
    private String id;

    @Field
    @NotNull(message = "Email cannot be null")
    @Email(message = "Email must be valid")
    private String email;

    @Field
    @NotNull(message = "Username cannot be null")
    @Length(min = 3, max = 20, message = "Username should be between 3 and 20 characters")
    private String username;

    @Field
    @NotNull(message = "Password cannot be null")
    @Length(min = 3, max = 20, message = "Password should be between 3 and 20 characters")
    private String password;

    @Transient
    private String role = "ROLE_USER";

    public User(String id, String email, String username, String password) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = "ROLE_USER";
    }
}
