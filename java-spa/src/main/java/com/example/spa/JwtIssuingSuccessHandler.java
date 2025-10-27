package com.example.spa;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtIssuingSuccessHandler implements AuthenticationSuccessHandler
{

    private final AppTokenGenerator tokenGenerator;

    public JwtIssuingSuccessHandler(AppTokenGenerator tokenGenerator) {
        this.tokenGenerator = tokenGenerator;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException
    {

        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
        String appToken = tokenGenerator.generate(oidcUser);

        Cookie cookie = new Cookie("APP_TOKEN", appToken);
        cookie.setHttpOnly(false);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(300);
        response.addCookie(cookie);

        response.sendRedirect("/");
    }
}

