FROM quay.io/keycloak/keycloak:latest AS builder


WORKDIR /opt/keycloak
# for demonstration purposes only, please make sure to use proper certificates in production instead
RUN keytool -genkeypair -storepass password -storetype PKCS12 -keyalg RSA -keysize 2048 -dname "CN=server" -alias server -ext "SAN:c=DNS:localhost,IP:127.0.0.1" -keystore conf/server.keystore
RUN /opt/keycloak/bin/kc.sh build

FROM quay.io/keycloak/keycloak:latest
COPY --from=builder /opt/keycloak/ /opt/keycloak/
COPY run.sh /opt/keycloak/run.sh

# Copy realm JSON into the container
COPY realm-export.json /opt/keycloak/data/import/realm-export.json

USER root

RUN chmod 775 /opt/keycloak/run.sh

USER 1000

ENTRYPOINT ["/opt/keycloak/run.sh"]
