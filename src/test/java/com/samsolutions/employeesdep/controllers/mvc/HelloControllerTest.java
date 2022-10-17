package com.samsolutions.employeesdep.controllers.mvc;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class HelloControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {

    }

    @Test
    @WithMockUser(username = "test_user", password = "test_pwd") // see test application.properties
    public void testHomePage() throws Exception {
        this.mockMvc
                .perform(
                        get("/hello?name=Mary")
                                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        equalTo("<!DOCTYPE html>\r\n<html>\r\n<head>\r\n    <meta charset=\"UTF-8\">\r\n    <title>Mary's page</title>\r\n</head>\r\n<body>\r\n<h2>Hello, <span>Mary</span></h2>\r\n\r\n<h2>th:each</h2>\r\n<ul>\r\n    \r\n        <li>Rose</li>\r\n    \r\n        <li>Lily</li>\r\n    \r\n        <li>Tulip</li>\r\n    \r\n        <li>Carnation</li>\r\n    \r\n        <li>Hyacinth</li>\r\n    \r\n</ul>\r\n</body>\r\n</html>")));
    }

    @Test
    @WithMockUser(username = "test_user", password = "test_pwd") // see test application.properties
    public void testNotFoundPage() throws Exception {
        this.mockMvc
                .perform(
                        get("/nonexisting_page")
                                .with(csrf()))
                .andExpect(status().isNotFound());
    }

}