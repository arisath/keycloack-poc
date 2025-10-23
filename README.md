# Keycloak POC

![Keycloak Logo](https://github.com/arisath/keycloack-poc/raw/main/flow.png)
A minimal **Java Spring Boot SPA** integrated with **Keycloak** using **OpenID Connect (OIDC)** for authentication.

This project demonstrates how to:

- Secure a Spring Boot application with Keycloak
- Use Thymeleaf to render user info after login
- Run Keycloak and your SPA locally using Docker or native Java
- Automate Keycloak realm and client setup


## Features

- Spring Boot 3.x / Java 17
- Spring Security 6.1+ with modern lambda DSL
- OAuth2 Login via Keycloak (OIDC)
- Session management via `JSESSIONID`
- Logout support from both SPA and Keycloak
- Dockerized Keycloak with automatic realm import

## Prerequisites

- Java 17+
- Maven 3.9+
- Docker & Docker Compose (optional, for containerized Keycloak)
- Keycloak 25+ (Quarkus distribution)

------
## Project Structure

```
Project Structure
keycloack-poc/
├─ spa/ # Spring Boot SPA
│ ├─ src/
│ │ ├─ main/
│ │ │ ├─ java/
│ │ │ │ └─ com/example/spa/
│ │ │ │ ├─ SpaApplication.java
│ │ │ │ ├─ SecurityConfig.java
│ │ │ │ └─ WebController.java
│ │ │ └─ resources/
│ │ │ └─ templates/
│ │ │ └─ index.html
├─ docker/
│ └─ keycloak/
│ └─ realm-export.json # Exported Keycloak realm
├─ pom.xml
└─ application.yml
```
------
## Features

- Runs Keycloak in **development mode** (`start-dev`)
- Configurable bootstrap admin username and password
- Easy setup for testing authentication and authorization flows
- Lightweight POC, suitable for local development

## Prerequisites

- **Java 17+** installed
- **Bash shell** (Linux/macOS/WSL)
- Keycloak distribution installed at `/opt/keycloak` 

## Setup

1. Clone this repository:

```bash
git clone https://github.com/arisath/keycloack-poc.git
cd keycloack-poc
