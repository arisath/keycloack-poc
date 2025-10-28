package com.example.spa;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LogoutController
{

    @GetMapping("/logout")
    public String logout(HttpServletResponse response)
    {

        // Remove the APP_TOKEN cookie
        Cookie cookie = new Cookie("APP_TOKEN", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0); // tells browser to delete it
        response.addCookie(cookie);

        return "redirect:/";

    }
}