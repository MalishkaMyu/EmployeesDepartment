package com.samsolutions.employeesdep.controllers.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.samsolutions.employeesdep.model.dto.UserDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

    @Autowired
    private UserController userController;

    @Autowired
    private MockMvc mockMvc;

    @Value("${spring.flyway.placeholders.admin_login}")
    private String adminLogin;

    private final List<UserDTO> testUsers = new ArrayList<>();

    private Long createUserMockPost(UserDTO userToSave, String loginToCheck) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String jsonReq = mapper.writeValueAsString(userToSave);
        MvcResult result = mockMvc.perform(
                        post("/api/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonReq)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.login", is(loginToCheck)))
                .andReturn();
        // receiving ID of saved object from the response
        String jsonResponse = result.getResponse().getContentAsString();
        return mapper.readValue(jsonResponse, UserDTO.class).getId();
    }

    @BeforeEach
    public void setup() throws Exception {
        Long userId;

        testUsers.add(0, new UserDTO("krosh", "kroshpwd123", "krosh@sam-solutions.com"));
        testUsers.add(1, new UserDTO("nyusha", "nyushapwd456", "nyusha@gmail.com"));
        testUsers.add(2, new UserDTO("pin", "pinpwd789", "pin@gmail.com"));

        // creating user 1
        userId = createUserMockPost(testUsers.get(0), "krosh");
        testUsers.get(0).setId(userId);

        // creating user 2
        userId = createUserMockPost(testUsers.get(1), "nyusha");
        testUsers.get(1).setId(userId);

        // creating user 3
        userId = createUserMockPost(testUsers.get(2), "pin");
        testUsers.get(2).setId(userId);
    }

    @Test
    @WithMockUser(username = "test_user", password = "test_pwd") // see test application.properties
    public void testReadAllUsers() throws Exception {
        // page 0
        mockMvc.perform(
                        get("/api/users")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(2)))
                .andExpect(jsonPath("$[0].login", is(adminLogin)));
        // page 1
        mockMvc.perform(
                        get("/api/users/page=1")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(2)));
    }

    @Test
    @WithMockUser(username = "test_user", password = "test_pwd") // see test application.properties
    public void testReadOneUser() throws Exception {
        Long userId = testUsers.get(1).getId();
        mockMvc.perform(
                        get("/api/users/" + userId.toString())
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login", is("nyusha")))
                .andExpect(jsonPath("$.email", is("nyusha@gmail.com")));
    }

    @Test
    @WithMockUser(username = "test_user", password = "test_pwd") // see test application.properties
    public void testReadUserNotFound() throws Exception {
        mockMvc.perform(
                        get("/api/users/118")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "test_user", password = "test_pwd") // see test application.properties
    public void testDeleteUserNotFound() throws Exception {
        mockMvc.perform(
                        delete("/api/users/118")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "test_user", password = "test_pwd") // see test application.properties
    public void testCreateUserDuplicate() throws Exception {
        UserDTO userToSave = new UserDTO("nyusha", "nyushapwd789", "nyusha@mail.ru");
        ObjectMapper mapper = new ObjectMapper();
        String jsonReq = mapper.writeValueAsString(userToSave);
        mockMvc.perform(
                        post("/api/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonReq)
                )
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockUser(username = "test_user", password = "test_pwd") // see test application.properties
    public void testUpdateUser() throws Exception {
        UserDTO userToSave = testUsers.get(1);
        userToSave.setLogin("losyash");
        userToSave.setEmail("losyash@gmail.com");
        userToSave.setPassword("losyashpwd234");
        Long userId = userToSave.getId();
        ObjectMapper mapper = new ObjectMapper();
        String jsonReq = mapper.writeValueAsString(userToSave);

        // updating Users
        mockMvc.perform(
                        put("/api/users/" + userId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonReq))
                .andExpect(status().isOk());

        // rereading updated user and check
        mockMvc.perform(
                        get("/api/users/" + userId)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login", is("losyash")))
                .andExpect(jsonPath("$.email", is("losyash@gmail.com")));

        // restore user data
        userToSave = testUsers.get(1);
        jsonReq = mapper.writeValueAsString(userToSave);
        mockMvc.perform(
                        put("/api/users/" + userId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonReq))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test_user", password = "test_pwd") // see test application.properties
    public void testUpdateUserDuplicate() throws Exception {
        UserDTO userToSave = testUsers.get(1);
        userToSave.setEmail("pin@gmail.com");
        ObjectMapper mapper = new ObjectMapper();
        String jsonReq = mapper.writeValueAsString(userToSave);

        // updating Users
        mockMvc.perform(
                        put("/api/users/" + userToSave.getId().toString())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonReq))
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockUser(username = "test_user", password = "test_pwd") // see test application.properties
    public void testUpdateUserNotFound() throws Exception {
        UserDTO userToSave = new UserDTO(118L, "bibi", "bibipwd345", "bibi@mail.ru");
        ObjectMapper mapper = new ObjectMapper();
        String jsonReq = mapper.writeValueAsString(userToSave);

        // updating Users
        mockMvc.perform(
                        put("/api/users/" + userToSave.getId().toString())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonReq))
                .andExpect(status().isNotFound());
    }

    @AfterEach
    public void tearDown() throws Exception {
        for (UserDTO user : testUsers) {
            mockMvc.perform(
                            delete("/api/users/" + user.getId().toString())
                                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }

        testUsers.clear();

        // check, whether all users except admin are deleted
        mockMvc.perform(
                        get("/api/users")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)))
                .andExpect(jsonPath("$[0].login", is(adminLogin)));
    }
}
