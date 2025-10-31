import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.File;
import java.util.List;

public class KeycloakUserImporter {

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Usage: java KeycloakUserImporter <users.json>");
            System.exit(1);
        }

        // Load users from JSON file
        ObjectMapper mapper = new ObjectMapper();
        List<UserRepresentation> users = mapper.readValue(new File(args[0]),
                new TypeReference<List<UserRepresentation>>() {});

        // Connect to Keycloak
        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl("http://localhost:8080")
                .realm("master") // admin realm
                .username("admin")
                .password("admin")
                .clientId("admin-cli")
                .grantType(OAuth2Constants.PASSWORD)
                .build();

        RealmResource realmResource = keycloak.realm("myrealm"); // target realm
        UsersResource usersResource = realmResource.users();

        // Create users
        for (UserRepresentation user : users) {
            // Create credential object if not present
            if (user.getCredentials() == null || user.getCredentials().isEmpty()) {
                CredentialRepresentation credential = new CredentialRepresentation();
                credential.setType(CredentialRepresentation.PASSWORD);
                credential.setValue("Password123!");
                credential.setTemporary(false);
                user.setCredentials(List.of(credential));
            }

            System.out.println("Creating user: " + user.getUsername());
            usersResource.create(user);
        }

        keycloak.close();
        System.out.println("User import complete.");
    }
}
