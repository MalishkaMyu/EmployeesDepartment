package com.samsolutions.employeesdep.controllers.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.samsolutions.employeesdep.model.dto.UserDTO;
import com.samsolutions.employeesdep.model.services.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerMockTest {

    private final List<UserDTO> testUsers = new ArrayList<>();

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        testUsers.add(0, new UserDTO(1L, "krosh", "kroshpwd123", "krosh@sam-solutions.com"));
        testUsers.add(1, new UserDTO(2L, "nyusha", "nyushapwd456", "myusha@gmail.com"));
        testUsers.add(2, new UserDTO(3L, "pin", "pinpwd789", "pin@gmail.com"));
    }

    @Test
    @WithMockUser(username = "test_user", password = "test_pwd", roles = {"user","admin"}) // see test application.properties
    public void testCreateUser() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String jsonStr = mapper.writeValueAsString(testUsers.get(0));

        when(userService.createUser(any(UserDTO.class))).thenReturn(testUsers.get(0));

        mockMvc.perform(
                        post("/api/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonStr)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.login", is("krosh")))
                .andExpect(jsonPath("$.email", is("krosh@sam-solutions.com")));
        verify(userService).createUser(any(UserDTO.class));
    }

    @Test
    @WithMockUser(username = "test_user", password = "test_pwd", roles = {"user","admin"}) // see test application.properties
    public void testReadOne() throws Exception {

        when(userService.getUserById(3L)).thenReturn(testUsers.get(2));

        mockMvc.perform(
                        get("/api/users/3")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.login", is("pin")))
                .andExpect(jsonPath("$.password", is("pinpwd789")))
                .andExpect(jsonPath("$.email", is("pin@gmail.com")));
        verify(userService).getUserById(anyLong());
    }

    @Test
    @WithMockUser(username = "test_user", password = "test_pwd", roles = {"user","admin"}) // see test application.properties
    public void testReadAll() throws Exception {

        when(userService.getAllUsers()).thenReturn(testUsers);

        mockMvc.perform(
                        get("/api/users")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(3)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].login", is("krosh")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].login", is("nyusha")))
                .andExpect(jsonPath("$[2].id", is(3)))
                .andExpect(jsonPath("$[2].login", is("pin")));
        verify(userService).getAllUsers();
    }

    @Test
    @WithMockUser(username = "test_user", password = "test_pwd", roles = {"user","admin"}) // see test application.properties
    public void testReadAllPaged() throws Exception {

        when(userService.getAllUsers(0)).thenReturn(testUsers.subList(0, 2));
        when(userService.getAllUsers(1)).thenReturn(testUsers.subList(2, 3));

        mockMvc.perform(
                        get("/api/users/page=0")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(2)))
                .andExpect(jsonPath("$[0].login", is("krosh")))
                .andExpect(jsonPath("$[1].login", is("nyusha")));
        mockMvc.perform(
                        get("/api/users/page=1")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)))
                .andExpect(jsonPath("$[0].login", is("pin")));
    }

    @Test
    @WithMockUser(username = "test_user", password = "test_pwd", roles = {"user","admin"}) // see test application.properties
    public void testUpdateUser() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String jsonStr = mapper.writeValueAsString(testUsers.get(2));

        when(userService.updateUser(any(UserDTO.class))).thenReturn(testUsers.get(2));

        mockMvc.perform(
                        put("/api/users/3")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonStr)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.login", is("pin")))
                .andExpect(jsonPath("$.email", is("pin@gmail.com")));
        verify(userService).updateUser(any(UserDTO.class));
    }

    @Test
    @WithMockUser(username = "test_user", password = "test_pwd", roles = {"user","admin"}) // see test application.properties
    public void testDeleteUser() throws Exception {
        when(userService.deleteUserById(anyLong())).thenReturn(true);

        mockMvc.perform(
                        delete("/api/users/2")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(userService).deleteUserById(anyLong());
    }

    @AfterEach
    public void tearDown() {
        testUsers.clear();
    }
}
