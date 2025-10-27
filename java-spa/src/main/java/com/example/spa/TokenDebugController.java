package com.example.spa;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;

@Controller
public class TokenDebugController {

    @GetMapping("/debug/tokens")
    public String debugTokens(Model model, HttpSession session, @AuthenticationPrincipal OidcUser user) {
        model.addAttribute("sessionId", session.getId());
        if (user != null) {
            model.addAttribute("idToken", user.getIdToken().getTokenValue());
            // If you need access token: OidcUser doesn't have it directly, so use OAuth2AuthorizedClientService
            model.addAttribute("accessToken", user.getIdToken().getTokenValue());
            model.addAttribute("claims", user.getClaims());
        }
        return "debug-tokens";
    }
}
