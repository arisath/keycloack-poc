# Keycloak POC

![Keycloak Logo](https://github.com/arisath/keycloack-poc/raw/main/keycloak-logo.png)

This project is a **proof of concept (POC)** for running Keycloak in development mode. It demonstrates how to quickly start a Keycloak server with a bootstrap admin user for testing and experimentation purposes.

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
