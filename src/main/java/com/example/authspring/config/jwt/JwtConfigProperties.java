package com.example.authspring.config.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "jwt")
public record JwtConfigProperties(String secret, Duration expiration, Duration refreshExpiration) {
}
