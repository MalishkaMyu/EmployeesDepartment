package com.samsolutions.employeesdep.controllers.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.samsolutions.employeesdep.model.dto.DepartmentDTO;
import com.samsolutions.employeesdep.model.dto.EmployeeDTO;
import com.samsolutions.employeesdep.model.dto.RoleDTO;
import com.samsolutions.employeesdep.model.dto.UserDTO;
import com.samsolutions.employeesdep.model.enums.Gender;
import com.samsolutions.employeesdep.model.services.EmployeeService;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
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
public class EmployeeControllerMockTest {

    private final List<EmployeeDTO> testEmps = new ArrayList<>();

    @MockBean
    private EmployeeService empService;

    @Autowired
    private EmployeeController empController;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        EmployeeDTO empToSave;
        UserDTO userToSave;
        DepartmentDTO depart1 = new DepartmentDTO(1L, "Java Department");
        DepartmentDTO depart2 = new DepartmentDTO(2L, ".NET Department");
        RoleDTO[] allRoles = {
                new RoleDTO(1L, "Java Programmer"),
                new RoleDTO(2L, ".NET programmer"),
                new RoleDTO(3L, "Tester"),
                new RoleDTO(4L, "Project manager"),
                new RoleDTO(5L, "Coffee drinker")
        };

        // creating and saving employee 1
        Set<RoleDTO> roles1 = Set.of(allRoles[0], allRoles[2], allRoles[4]); // Java-Roles
        empToSave = new EmployeeDTO(1L, "Krosh", "Smesharik", Gender.MALE,
                LocalDate.of(2010, 10, 11),
                LocalDate.of(2020, 7, 20));
        userToSave = new UserDTO(101L, "krosh", "kroshpwd", "krosh@gmail.com");
        empToSave.setUser(userToSave);
        empToSave.setDepartment(depart1);
        empToSave.setEmployeeRoles(roles1);
        testEmps.add(0, empToSave);

        // creating and saving employee 2
        Set<RoleDTO> roles2 = Set.of(allRoles[3], allRoles[4]); // PM-roles
        empToSave = new EmployeeDTO(2L, "Nyusha", "Smesharik", Gender.FEMALE,
                LocalDate.of(2010, 1, 13), null);
        userToSave = new UserDTO(102L, "nyusha", "nyushapwd", "nyusha@gmail.com");
        empToSave.setUser(userToSave);
        empToSave.setDepartment(depart1);
        empToSave.setEmployeeRoles(roles2);
        testEmps.add(1, empToSave);

        // creating and saving employee 3
        Set<RoleDTO> roles3 = Set.of(allRoles[1]); // .NET roles
        empToSave = new EmployeeDTO(3L, "Pin", "Smesharik", Gender.MALE,
                LocalDate.of(2008, 8, 21),
                LocalDate.of(2020, 3, 3));
        userToSave = new UserDTO(103L, "pin", "pinpwd123$", "pin@mail.ru");
        empToSave.setUser(userToSave);
        empToSave.setDepartment(depart2);
        empToSave.setEmployeeRoles(roles3);
        testEmps.add(2, empToSave);
    }

    @Test
    @WithMockUser(username = "test_user", password = "test_pwd") // see test application.properties
    public void testReadOne() throws Exception {

        when(empService.getEmployeeById(3L)).thenReturn(testEmps.get(2));

        mockMvc.perform(
                        get("/api/employees/3")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.name", is("Pin")))
                .andExpect(jsonPath("$.surname", is("Smesharik")))
                .andExpect(jsonPath("$.user.id", is(103)))
                .andExpect(jsonPath("$.user.login", is("pin")))
                .andExpect(jsonPath("$.department.name", is(".NET Department")))
                .andExpect(jsonPath("$.employeeRoles.size()", is(1)))
                .andExpect(jsonPath("$.employeeRoles[0].role", is(".NET programmer")));
        verify(empService).getEmployeeById(anyLong());
    }

    @Test
    @WithMockUser(username = "test_user", password = "test_pwd") // see test application.properties
    public void testReadEmployeesInDepartment() throws Exception {

        when(empService.getEmployeesToDepartment("Java Department")).thenReturn(testEmps.subList(0, 2));

        mockMvc.perform(
                        get("/api/employees/department=Java Department")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(2)))
                .andExpect(jsonPath("$[0].name", is("Krosh")))
                .andExpect(jsonPath("$[0].surname", is("Smesharik")))
                .andExpect(jsonPath("$[1].name", is("Nyusha")))
                .andExpect(jsonPath("$[1].surname", is("Smesharik")));
        verify(empService).getEmployeesToDepartment(anyString());
    }

    @Test
    @WithMockUser(username = "test_user", password = "test_pwd") // see test application.properties
    public void testCreateEmployee() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        String jsonStr = mapper.writeValueAsString(testEmps.get(0));

        when(empService.createEmployee(any(EmployeeDTO.class))).thenReturn(testEmps.get(0));

        mockMvc.perform(
                        post("/api/employees")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonStr)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Krosh")))
                .andExpect(jsonPath("$.surname", is("Smesharik")))
                .andExpect(jsonPath("$.department.name", is("Java Department")))
                .andExpect(jsonPath("$.employeeRoles.size()", is(3)));
        verify(empService).createEmployee(any(EmployeeDTO.class));
    }

    @Test
    @WithMockUser(username = "test_user", password = "test_pwd") // see test application.properties
    public void testUpdateEmployee() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        String jsonStr = mapper.writeValueAsString(testEmps.get(2));

        when(empService.updateEmployee(any(EmployeeDTO.class))).thenReturn(testEmps.get(2));

        mockMvc.perform(
                        put("/api/employees/3")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonStr)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.name", is("Pin")))
                .andExpect(jsonPath("$.surname", is("Smesharik")))
                .andExpect(jsonPath("$.department.name", is(".NET Department")))
                .andExpect(jsonPath("$.employeeRoles.size()", is(1)));
        verify(empService).updateEmployee(any(EmployeeDTO.class));
    }

    @Test
    @WithMockUser(username = "test_user", password = "test_pwd") // see test application.properties
    public void testDeleteEmployee() throws Exception {
        when(empService.deleteEmployeeById(anyLong())).thenReturn(true);

        mockMvc.perform(
                        delete("/api/employees/2")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(empService).deleteEmployeeById(anyLong());
    }

    @AfterEach
    public void tearDown() {
        testEmps.clear();
    }
}
