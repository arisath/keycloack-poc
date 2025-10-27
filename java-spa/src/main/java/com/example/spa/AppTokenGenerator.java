package com.example.spa;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

@Component
public class AppTokenGenerator {

    private final Algorithm algorithm;

    public Algorithm getAlgorithm() {
        return algorithm;
    }
    public AppTokenGenerator() {
        try {
            RSAPrivateKey privateKey = loadPrivateKey("keys/app-private.pem");
            RSAPublicKey publicKey = loadPublicKey("keys/app-public.pem");
            this.algorithm = Algorithm.RSA256(publicKey, privateKey);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load RSA keys", e);
        }
    }

    public String generate(OidcUser oidcUser) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(300);

        return JWT.create()
                .withIssuer("https://yourapp.local")
                .withSubject(oidcUser.getSubject())
                .withClaim("email", oidcUser.getEmail())
                .withArrayClaim("roles",
                        oidcUser.getAuthorities().stream()
                                .map(Object::toString)
                                .toArray(String[]::new))
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(exp))
                .sign(algorithm);
    }


    private static RSAPrivateKey loadPrivateKey(String classpathLocation) throws Exception {
        try (InputStream inputStream = new ClassPathResource(classpathLocation).getInputStream()) {
            String key = new String(inputStream.readAllBytes())
                    .replaceAll("-----BEGIN (RSA )?PRIVATE KEY-----", "")
                    .replaceAll("-----END (RSA )?PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");
            byte[] decoded = Base64.getDecoder().decode(key);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
            return (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(spec);
        }
    }

    private static RSAPublicKey loadPublicKey(String classpathLocation) throws Exception {
        try (InputStream inputStream = new ClassPathResource(classpathLocation).getInputStream()) {
            String key = new String(inputStream.readAllBytes())
                    .replaceAll("-----BEGIN (RSA )?PUBLIC KEY-----", "")
                    .replaceAll("-----END (RSA )?PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");
            byte[] decoded = Base64.getDecoder().decode(key);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
            return (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(spec);
        }
    }
}
