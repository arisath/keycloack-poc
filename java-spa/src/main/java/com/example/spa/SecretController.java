package com.example.spa;

import com.example.spa.model.AppUserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SecretController {

    @GetMapping("/secret")
    public String secretPage(Authentication auth, Model model) {
        if (auth != null && auth.isAuthenticated()) {
            AppUserPrincipal principal = (AppUserPrincipal) auth.getPrincipal();

            model.addAttribute("name", principal.getName());
            model.addAttribute("email", principal.getEmail()); // or fetch from JWT if needed
            model.addAttribute("balance", (int)(Math.random() * 9000 + 1000));
        }
        return "secret"; // Thymeleaf template: secret.html
    }
}
