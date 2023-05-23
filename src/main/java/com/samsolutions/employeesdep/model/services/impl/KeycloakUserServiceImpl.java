package com.samsolutions.employeesdep.model.services.impl;

import com.samsolutions.employeesdep.exception.StatusWrongException;
import com.samsolutions.employeesdep.model.dto.UserKeycloakDTO;
import com.samsolutions.employeesdep.model.services.KeycloakUserService;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;

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

        // user roles
        /*Map<String, List<String>> clientRoles = new HashMap<>();
        clientRoles.put(kcClientId, userDTO.getRoles());
        user.setClientRoles(clientRoles);*/

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

        // vom https://stackoverflow.com/questions/43222769/how-to-create-keycloak-client-role-programmatically-and-assign-to-user
        // third API-Keycloak. Assign roles to the user
        /*realmResource.clients().findByClientId(kcClientId)
                .forEach(clientRepresentation -> {
                    List<RoleRepresentation> keycloakRoles = new ArrayList<>();
                    for (String role: userDTO.getRoles()) {
                        RoleRepresentation savedRoleRepresentation = realmResource.clients()
                                .get(clientRepresentation.getId()).roles().get(role).toRepresentation();
                        if (savedRoleRepresentation != null) {
                            keycloakRoles.add(savedRoleRepresentation);
                        }
                    }
                    realmResource.users().get(keycloakId).roles().clientLevel(clientRepresentation.getId())
                            .add(keycloakRoles);
                });*/
        // vom https://medium.com/chain-analytica/keycloak-work-with-client-roles-in-spring-boot-a34d74947c93
        /*ClientResource clientResource = realmResource.clients().get(kcClientId);
        List<RoleRepresentation> rolesToAdd = new ArrayList<>();
        for (String role: userDTO.getRoles()) {
            if (clientResource.roles().get(role) != null) {
                RoleRepresentation roleRepr = clientResource.roles().get(role).toRepresentation();
                rolesToAdd.add(roleRepr);
            }
        }
        userResource.roles().clientLevel(kcClientId).add(rolesToAdd);*/

        return userDTO;
    }

    @Override
    public UserKeycloakDTO updateKeycloakUser(UserKeycloakDTO userDTO) {
        return null;
    }

    @Override
    public void deleteKeycloakUser(String keycloakID) {
        RealmResource realmResource = keycloakServiceAccount.realm(kcRealm);
        realmResource.users().get(keycloakID).remove();
    }
}
