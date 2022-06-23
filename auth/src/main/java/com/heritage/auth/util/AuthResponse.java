package com.heritage.auth.util;

public class AuthResponse {
    private String jwt;
    private String username;
    private String email;

    public AuthResponse(){
    }

    public AuthResponse(String jwt, String username, String email) {
        this.jwt = jwt;
        this.username = username;
        this.email = email;
    }

    public String getJwt() {
        return jwt;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }
}
