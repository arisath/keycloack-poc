package com.example.spa;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SecretController {

    @GetMapping("/secret")
    public String secretPage(Authentication auth, Model model) {
        if (auth != null && auth.isAuthenticated()) {
            String username = (String) auth.getPrincipal(); // because you stored the username string
            model.addAttribute("name", username);
            model.addAttribute("email", username + "@example.com"); // or fetch from JWT if needed
            model.addAttribute("balance", (int)(Math.random() * 9000 + 1000));
        }
        return "secret"; // Thymeleaf template: secret.html
    }
}
