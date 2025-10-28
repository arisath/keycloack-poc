package com.example.spa;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;

@Controller
public class TokenDebugController {

    private final Algorithm algorithm;

    public TokenDebugController(Algorithm algorithm) {
        this.algorithm = algorithm; // Same Algorithm used to sign APP_TOKEN
    }

    @GetMapping("/debug/tokens")
    public String debugTokens(Model model, HttpServletRequest request) {

        // Optional: show session ID
        model.addAttribute("sessionId", request.getSession().getId());

        // Find the APP_TOKEN cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            Arrays.stream(cookies)
                    .filter(c -> "APP_TOKEN".equals(c.getName()))
                    .findFirst()
                    .ifPresent(cookie -> {
                        try {
                            DecodedJWT decoded = JWT
                                    .require(algorithm)
                                    .build()
                                    .verify(cookie.getValue());

                            // Extract fields from your JWT
                            String username = decoded.getSubject();
                            String email = decoded.getClaim("email").asString();
                            String idToken = decoded.getClaim("idToken").asString(); // OIDC token stored as claim
                            String accessToken = decoded.getClaim("accessToken").asString(); // optional

                            model.addAttribute("username", username);
                            model.addAttribute("email", email);
                            model.addAttribute("idToken", idToken);
                            model.addAttribute("accessToken", accessToken);
                            model.addAttribute("claims", decoded.getClaims());

                        } catch (Exception e) {
                            model.addAttribute("error", "Invalid APP_TOKEN: " + e.getMessage());
                        }
                    });
        }

        return "debug-tokens";
    }
}
