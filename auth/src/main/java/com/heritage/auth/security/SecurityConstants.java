package com.heritage.auth.security;

import org.springframework.beans.factory.annotation.Value;

public class SecurityConstants {

    public static final String SECRET = "${security.jwt.secret}";
    public static final long EXPIRATION_TIME = 1000*60*60*10; // 15 mins
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
}
