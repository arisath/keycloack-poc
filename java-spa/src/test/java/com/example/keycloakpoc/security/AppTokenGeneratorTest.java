package com.example.keycloakpoc.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.spa.AppTokenGenerator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AppTokenGeneratorTest {

    private static AppTokenGenerator tokenGenerator;

    @BeforeAll
    static void setup() {
        tokenGenerator = new AppTokenGenerator();
    }

    @Test
    void testGenerateJwtFromOidcUser() {
        // Mock OIDC user
        OidcIdToken idToken = new OidcIdToken(
                "mock-id-token",
                Instant.now(),
                Instant.now().plusSeconds(600),
                Map.of(
                        "sub", "12345",
                        "email", "user@example.com"
                )
        );
        OidcUser oidcUser = new DefaultOidcUser(
                List.of(new SimpleGrantedAuthority("ROLE_USER")),
                idToken,
                "email"
        );

        // Generate JWT
        String jwt = tokenGenerator.generate(oidcUser);
        assertNotNull(jwt, "Generated JWT should not be null");

        // Decode and verify JWT
        DecodedJWT decoded = JWT.decode(jwt);
        assertEquals("https://yourapp.local", decoded.getIssuer());
        assertEquals("12345", decoded.getSubject());
        assertEquals("user@example.com", decoded.getClaim("email").asString());
        assertNotNull(decoded.getClaim("roles").asArray(String.class));

        // Verify signature with same public key
        try {
            Algorithm algorithm = tokenGenerator.getAlgorithm(); // optional getter you can add
            JWT.require(algorithm)
                    .withIssuer("https://yourapp.local")
                    .build()
                    .verify(jwt);
        } catch (Exception e) {
            fail("JWT signature verification failed: " + e.getMessage());
        }
    }
}
