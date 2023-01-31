package com.samsolutions.employeesdep.controllers.rest;

import com.samsolutions.employeesdep.model.dto.UserDTO;
import com.samsolutions.employeesdep.model.services.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class UserControllerUnitTest {

    private final List<UserDTO> testUsers = new ArrayList<>();

    @MockBean
    private UserService userService;

    @Autowired
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .build();

        UserDTO userDTO = new UserDTO("krosh", "kroshpwd123", "krosh@sam-solutions.com");
        testUsers.add(0, userDTO);
        userDTO = new UserDTO("nyusha", "nyushapwd456", "myusha@gmail.com");
        testUsers.add(1, userDTO);
        userDTO = new UserDTO("pin", "pinpwd789", "pin@gmail.com");
        testUsers.add(2, userDTO);
    }

    @Test
    public void TestReadAll() throws Exception {
        Mockito.when(userService.getAllUsers()).thenReturn(testUsers);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/users")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].login", Matchers.is("krosh")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].login", Matchers.is("nyusha")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].login", Matchers.is("pin")));
    }
}
