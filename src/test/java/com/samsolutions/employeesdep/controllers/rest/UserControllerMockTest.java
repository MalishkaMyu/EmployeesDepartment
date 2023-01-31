package com.samsolutions.employeesdep.controllers.rest;

import com.samsolutions.employeesdep.model.dto.UserDTO;
import com.samsolutions.employeesdep.model.services.UserService;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerMockTest {

    private final List<UserDTO> testUsers = new ArrayList<>();

    @MockBean
    private UserService userService;

    @Autowired
    private UserController userController;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {

        UserDTO userDTO = new UserDTO("krosh", "kroshpwd123", "krosh@sam-solutions.com");
        testUsers.add(0, userDTO);
        userDTO = new UserDTO("nyusha", "nyushapwd456", "myusha@gmail.com");
        testUsers.add(1, userDTO);
        userDTO = new UserDTO("pin", "pinpwd789", "pin@gmail.com");
        testUsers.add(2, userDTO);

        when(userService.getAllUsers()).thenReturn(testUsers);
    }

    @Test
    @WithMockUser(username = "test_user", password = "test_pwd") // see test application.properties
    public void testReadAll() throws Exception {

        mockMvc.perform(
                        get("/api/users")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(3)))
                .andExpect(jsonPath("$[0].login", is("krosh")))
                .andExpect(jsonPath("$[1].login", is("nyusha")))
                .andExpect(jsonPath("$[2].login", is("pin")));
    }
}
