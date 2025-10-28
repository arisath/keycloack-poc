package com.example.spa;

import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppTokenJWTConfig
{

    private final AppTokenGenerator tokenGenerator;

    public AppTokenJWTConfig(AppTokenGenerator tokenGenerator) {
        this.tokenGenerator = tokenGenerator;
    }

    @Bean
    public Algorithm jwtAlgorithm() {
        return tokenGenerator.getAlgorithm(); // same algorithm used for signing
    }
}
