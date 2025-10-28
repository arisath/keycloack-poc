package com.example.spa.model;

import java.util.List;

public class AppUserPrincipal {
    private final String username;
    private final String name;
    private final String email;
    private final List<String> roles;

    public AppUserPrincipal(String username, String name, String email, List<String> roles) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.roles = roles;
    }

    public String getUsername() { return username; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public List<String> getRoles() { return roles; }
}
