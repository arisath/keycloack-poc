package com.example.spa;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/")
    public String index(Model model, @AuthenticationPrincipal OidcUser user) {
        if (user != null) {
            // User is logged in via Keycloak
            model.addAttribute("name", user.getFullName());
            model.addAttribute("email", user.getEmail());
            model.addAttribute("jwtClaims", user.getClaims());

        }
        return "index"; // Thymeleaf template src/main/resources/templates/index.html
    }
}
