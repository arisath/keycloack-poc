package com.example.spa;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorPageController {
    @GetMapping("/unauthorized")
    public String unauthorized() {
        return "error/401"; // Thymeleaf template
    }
}
