#!/bin/bash

set -e
cd /opt/keycloak/
ls

# Source the secrets file if it exists
if [ -f "./secrets.sh" ]; then
  echo "Loading secrets..."
  source ./secrets.sh
else
  echo "No secrets file found, proceeding without it"
fi

REALM_TEMPLATE="/opt/keycloak/data/import/realm-export.json.template"
REALM_FILE="/opt/keycloak/data/import/realm-export.json"

# Replace placeholders in the template with environment variables using sed
if [ -f "$REALM_TEMPLATE" ]; then
  echo "Applying environment variables to Keycloak realm template..."

  sed -e "s|\${CLIENT_ID}|${CLIENT_ID}|g" \
      -e "s|\${CLIENT_SECRET}|${CLIENT_SECRET}|g" \
      "$REALM_TEMPLATE" > "$REALM_FILE"
else
  echo "❌ Realm template not found at $REALM_TEMPLATE"
  exit 1
fi

/opt/keycloak/bin/kc.sh start-dev \
  --bootstrap-admin-username=admin \
  --bootstrap-admin-password=admin \
  --import-realm \
  --features=passkeys,passkeys-conditional-ui-authenticator
