package com.example.authservice.until;

public final class SecurityConstants {
    public static final String[] PUBLIC_URLS = {
            "/auth/register",
            "/auth/login",
            "/auth/refresh",
            "/v3/api-docs/**",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/webjars/swagger-ui/**"
    };
}
