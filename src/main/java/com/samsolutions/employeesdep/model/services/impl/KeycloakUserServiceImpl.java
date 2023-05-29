package com.samsolutions.employeesdep.model.services.impl;

import com.samsolutions.employeesdep.exception.StatusWrongException;
import com.samsolutions.employeesdep.model.dto.UserKeycloakDTO;
import com.samsolutions.employeesdep.model.services.KeycloakUserService;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Service
public class KeycloakUserServiceImpl implements KeycloakUserService {

    @Value("${keycloak.realm}")
    private String kcRealm;

    @Value("${keycloak.resource}")
    private String kcClientId;

    @Autowired
    Keycloak keycloakServiceAccount;

    @Override
    public UserKeycloakDTO createKeycloakUser(UserKeycloakDTO userDTO) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(userDTO.getLogin());
        user.setFirstName(userDTO.getName());
        user.setLastName(userDTO.getSurname());

        user.setEmail(userDTO.getEmail());
        user.setEmailVerified(true);
        user.setEnabled(true);

        // first call KeycloakAPI - user saving
        RealmResource realmResource = keycloakServiceAccount.realm(kcRealm);
        Response response = realmResource.users().create(user);

        if (!response.getStatusInfo().equals(Response.Status.CREATED)) {
            Response.StatusType statusInfo = response.getStatusInfo();
            throw new StatusWrongException(
                    "Keycloak-Create method returned status " + statusInfo.getReasonPhrase() +
                            " (Code: " + statusInfo.getStatusCode() + ")",
                    UserKeycloakDTO.class);
        }

        String keycloakId = CreatedResponseUtil.getCreatedId(response);
        userDTO.setKeycloakId(keycloakId);

        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(false);
        passwordCred.setType("password");
        passwordCred.setValue(userDTO.getPassword());

        // second call KeycloakAPI - password saving
        UserResource userResource = realmResource.users().get(keycloakId);
        userResource.resetPassword(passwordCred);

        // third call KeycloakAPI - user roles
        // vom https://medium.com/chain-analytica/keycloak-work-with-client-roles-in-spring-boot-a34d74947c93
        ClientRepresentation clientRep = realmResource.clients().findByClientId(kcClientId).get(0);
        String clientID = clientRep.getId();
        ClientResource clientResource = realmResource.clients().get(clientID);

        // receiving available roles from keycloak
        List<String> availableRoles = clientResource.roles().list().stream()
                .map(RoleRepresentation :: getName)
                .toList();

        List<RoleRepresentation> rolesToAdd = new ArrayList<>();
        for (String role : userDTO.getRoles()) {
            // check whether user role is contained in the keycloak roles
            if (availableRoles.contains(role)) {
                rolesToAdd.add(clientResource
                        .roles().get(role).toRepresentation());
            }
        }
        // saving roles in keycloak
        userResource.roles().clientLevel(clientID).add(rolesToAdd);

        return userDTO;
    }

    @Override
    public UserKeycloakDTO updateKeycloakUser(UserKeycloakDTO userDTO) {
        // read keycloak user to the keycloak-id
        RealmResource realmResource = keycloakServiceAccount.realm(kcRealm);
        UserResource userResource = realmResource.users().get(userDTO.getKeycloakId());
        UserRepresentation user = userResource.toRepresentation();

        // set new user attributes
        user.setUsername(userDTO.getLogin());
        user.setFirstName(userDTO.getName());
        user.setLastName(userDTO.getSurname());
        user.setEmail(userDTO.getEmail());

        // first call KeycloakAPI - update user date
        userResource.update(user);

        // saving password if not empty
        if (!userDTO.getPassword().isEmpty()) {
            CredentialRepresentation passwordCred = new CredentialRepresentation();
            passwordCred.setTemporary(false);
            passwordCred.setType("password");
            passwordCred.setValue(userDTO.getPassword());

            // second call KeycloakAPI - password saving
            userResource.resetPassword(passwordCred);
        }

        // third call KeycloakAPI - user roles
        // vom https://medium.com/chain-analytica/keycloak-work-with-client-roles-in-spring-boot-a34d74947c93
        ClientRepresentation clientRep = realmResource.clients().findByClientId(kcClientId).get(0);
        String clientID = clientRep.getId();
        ClientResource clientResource = realmResource.clients().get(clientID);

        // receiving available roles from keycloak
        List<String> availableRoles = clientResource.roles().list().stream()
                .map(RoleRepresentation :: getName)
                .toList();

        List<RoleRepresentation> rolesToAdd = new ArrayList<>();
        for (String role : userDTO.getRoles()) {
            // check whether user role is contained in the keycloak roles
            if (availableRoles.contains(role)) {
                rolesToAdd.add(clientResource
                        .roles().get(role).toRepresentation());
            }
        }
        // saving roles in keycloak - first deleting old, then add new
        List<RoleRepresentation> oldRoles = userResource.roles().clientLevel(clientID).listAll();
        userResource.roles().clientLevel(clientID).remove(oldRoles);
        userResource.roles().clientLevel(clientID).add(rolesToAdd);
        return userDTO;
    }

    @Override
    public boolean deleteKeycloakUser(String keycloakID) {
        RealmResource realmResource = keycloakServiceAccount.realm(kcRealm);
        Response response = realmResource.users().delete(keycloakID);

        return response.getStatusInfo().equals(Response.Status.NO_CONTENT);
    }
}
