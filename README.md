# Keycloak POC

![Keycloak Logo](https://github.com/arisath/keycloack-poc/raw/main/flow.png)
A minimal **Java Spring Boot SPA** integrated with **Keycloak** using **OpenID Connect (OIDC)** for authentication.

This project demonstrates how to:

- Secure a Spring Boot application with Keycloak
- Use Thymeleaf to render user info after login
- Run Keycloak and your SPA locally using Docker or native Java
- Automate Keycloak realm and client setup

## Architecture

- **Keycloak**: Acts as the Identity Provider (IdP) for authenticating users via OIDC.
- **Spring Boot Application**: Serves as the client that handles user authentication and authorization.
- **JWT (`APP_TOKEN`)**: A custom JWT issued by the Spring Boot application upon successful login, used for stateless session management.

## Authentication Flow

1. **User Login**:
    - The user accesses the application and is redirected to Keycloak for authentication.
    - Upon successful authentication, Keycloak redirects the user back to the application with an authorization code.

2. **Token Exchange**:
    - The application exchanges the authorization code for an ID token and access token from Keycloak.
    - The ID token is parsed to extract user details and roles.

3. **Custom JWT Issuance**:
    - The application generates a custom JWT (`APP_TOKEN`) containing user information and roles.
    - This JWT is set as a secure, HTTP-only cookie in the user's browser.

4. **Stateless Session Management**:
    - Subsequent requests include the `APP_TOKEN` cookie.
    - A custom filter (`AppJwtAuthenticationFilter`) intercepts requests, validates the `APP_TOKEN`, and sets the authentication context if valid.
    - No server-side session is maintained; authentication is stateless.

5. **Access Control**:
    - Endpoints like `/secret` are protected and require valid authentication.
    - Access is granted based on the presence and validity of the `APP_TOKEN` cookie.

## Key Components

### 1. Security Configuration (`SecurityConfig`)

Configures Spring Security to:

- Disable session creation (`SessionCreationPolicy.STATELESS`).
- Implement custom authentication filters.
- Define authorization rules for endpoints.

### 2. Authentication Success Handler (`JwtIssuingSuccessHandler`)

Handles the successful authentication event by:

- Generating the `APP_TOKEN` JWT.
- Setting the `APP_TOKEN` as a secure, HTTP-only cookie.
- Redirecting the user to the home page.

### 3. JWT Authentication Filter (`AppJwtAuthenticationFilter`)

Intercepts incoming requests to:

- Extract and validate the `APP_TOKEN` cookie.
- Set the authentication context if the token is valid.
- Reject requests with invalid or missing tokens.

## Logout Mechanism

To log out:

- **Delete the `APP_TOKEN` cookie**: This removes the user's session token.
- **Invalidate the Keycloak session**: Redirect the user to Keycloak's end-session endpoint to terminate the session on the IdP side.


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
