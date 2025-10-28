package com.example.spa;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final JwtIssuingSuccessHandler successHandler;

    public SecurityConfig(JwtIssuingSuccessHandler successHandler) {
        this.successHandler = successHandler; // Spring will automatically inject
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   AppTokenGenerator tokenGenerator) throws Exception {
        http
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/unauthorized","/public/**").permitAll()
                        .requestMatchers("/secret").authenticated()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth -> oauth
                        .successHandler(successHandler) // issues APP_TOKEN cookie
                )

                // Instead of default redirect, send 401 if not authenticated
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.sendRedirect("/unauthorized");
                          //  response.sendError(HttpServletResponse.SC_UNAUTHORIZED);

                        })
                )

                .addFilterBefore(new AppJwtAuthenticationFilter(tokenGenerator.getAlgorithm()),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    }