package com.example.spa;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SecretController {

    @GetMapping("/secret")
    public String secretPage(@AuthenticationPrincipal OidcUser oidcUser, Model model) {
        if (oidcUser != null) {
            model.addAttribute("name", oidcUser.getFullName());
            model.addAttribute("email", oidcUser.getEmail());
            model.addAttribute("balance", (int)(Math.random() * 9000 + 1000)); // 1000-9999 GBP

        }
        return "secret"; // Thymeleaf template: secret.html
    }
}
