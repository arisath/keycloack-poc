package com.example.spa;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.spa.model.AppUserPrincipal;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AppJwtAuthenticationFilter extends OncePerRequestFilter {

    private final Algorithm algorithm;

    public AppJwtAuthenticationFilter(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            Arrays.stream(cookies)
                    .filter(c -> "APP_TOKEN".equals(c.getName()))
                    .findFirst()
                    .ifPresent(cookie -> {
                        try {
                            DecodedJWT decoded = JWT.require(algorithm)
                                    .build()
                                    .verify(cookie.getValue());

                            // Extract claims
                            String username = decoded.getSubject();
                            String name = decoded.getClaim("name").asString();
                            String email = decoded.getClaim("email").asString();
                            List<String> roles = decoded.getClaim("roles").asList(String.class);

                            // Create custom principal
                            AppUserPrincipal principal = new AppUserPrincipal(username, name, email, roles);


                            List<SimpleGrantedAuthority> authorities = roles.stream()
                                    .map(SimpleGrantedAuthority::new)
                                    .collect(Collectors.toList());

                            UsernamePasswordAuthenticationToken auth =
                                    new UsernamePasswordAuthenticationToken(principal, null, authorities);

                            SecurityContextHolder.getContext().setAuthentication(auth);

                        } catch (Exception e) {
                            SecurityContextHolder.clearContext();
                        }
                    });
        }
        filterChain.doFilter(request, response);
    }
}
