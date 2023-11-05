package com.deemor.motif.security.configuration;

import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

@AllArgsConstructor
@Configuration
public class SecretKeyConfiguration {

    private JwtConfiguration jwtConfiguration;

    @Bean
    public SecretKey secretKeyAccessToken() {
        return Keys.hmacShaKeyFor(jwtConfiguration.getSecretKeyAccessToken().getBytes());
    }

    @Bean
    public SecretKey secretKeyRefreshToken() {
        return Keys.hmacShaKeyFor(jwtConfiguration.getSecretKeyRefreshToken().getBytes());
    }
}