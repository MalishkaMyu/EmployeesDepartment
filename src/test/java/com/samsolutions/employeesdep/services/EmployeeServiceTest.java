package com.samsolutions.employeesdep.services;

import com.samsolutions.employeesdep.config.MyPasswordEncoder;
import com.samsolutions.employeesdep.exception.EntityDuplicateException;
import com.samsolutions.employeesdep.model.dao.JpaRoleDao;
import com.samsolutions.employeesdep.model.dto.DepartmentDTO;
import com.samsolutions.employeesdep.model.dto.EmployeeDTO;
import com.samsolutions.employeesdep.model.dto.RoleDTO;
import com.samsolutions.employeesdep.model.dto.UserDTO;
import com.samsolutions.employeesdep.model.enums.Gender;
import com.samsolutions.employeesdep.model.repository.DepartmentRepository;
import com.samsolutions.employeesdep.model.repository.UserRepository;
import com.samsolutions.employeesdep.model.services.EmployeeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class EmployeeServiceTest {
    @Value("${spring.flyway.placeholders.admin_login}")
    private String adminLogin;
    @Value("${spring.flyway.placeholders.default_department}")
    private String defaultDepartment;

    @Autowired
    private EmployeeService empService;

    @Autowired
    private DepartmentRepository departRepository;

    @Autowired
    private JpaRoleDao roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MyPasswordEncoder encoder;

    private final List<Long> listIDs = new ArrayList<>();

    private void updateRoles(RoleDTO[] allRoles, Set<RoleDTO> savedRoles) {
        for (RoleDTO savedRole : savedRoles) {
            for (RoleDTO role : allRoles) {
                if (role.getRole().equals(savedRole.getRole())) {
                    role.setId(savedRole.getId());
                    break;
                }
            }
        }
    }

    @BeforeEach
    public void setup() {
        EmployeeDTO empToSave, savedEmp;
        UserDTO userToSave;
        DepartmentDTO depart1 = new DepartmentDTO("Java Department");
        DepartmentDTO depart2 = new DepartmentDTO(".NET Department");
        RoleDTO[] allRoles = {new RoleDTO("Java Programmer"), new RoleDTO(".NET programmer"),
                new RoleDTO("Tester"), new RoleDTO("Project manager"), new RoleDTO("coffee drinker")
        };

        // creating and saving employee 1
        Set<RoleDTO> roles1 = Set.of(allRoles[0], allRoles[2], allRoles[4]); // Java-Roles
        empToSave = new EmployeeDTO("Krosh", "Smesharik", Gender.MALE,
                LocalDate.of(2010, 10, 11),
                LocalDate.of(2020, 7, 20));
        userToSave = new UserDTO("krosh", "kroshpwd", "krosh@gmail.com");
        empToSave.setUser(userToSave);
        empToSave.setDepartment(depart1);
        empToSave.setEmployeeRoles(roles1);
        savedEmp = empService.createEmployee(empToSave);
        if (savedEmp.getId() != null) {
            listIDs.add(savedEmp.getId());
            depart1 = savedEmp.getDepartment();
            updateRoles(allRoles, savedEmp.getEmployeeRoles());
        }

        // creating and saving employee 2
        Set<RoleDTO> roles2 = Set.of(allRoles[3], allRoles[4]); // PM-roles
        empToSave = new EmployeeDTO("Nyusha", "Smesharik", Gender.FEMALE,
                LocalDate.of(2010, 1, 13), null);
        userToSave = new UserDTO("nyusha", "nyushapwd", "nyusha@gmail.com");
        empToSave.setUser(userToSave);
        empToSave.setDepartment(depart1);
        empToSave.setEmployeeRoles(roles2);
        savedEmp = empService.createEmployee(empToSave);
        if (savedEmp.getId() != null) {
            listIDs.add(savedEmp.getId());
            updateRoles(allRoles, savedEmp.getEmployeeRoles());
        }

        // creating and saving employee 3
        Set<RoleDTO> roles3 = Set.of(allRoles[1]); // .NET roles
        empToSave = new EmployeeDTO("Pin", "Smesharik", Gender.MALE,
                LocalDate.of(2008, 8, 21),
                LocalDate.of(2020, 3, 3));
        userToSave = new UserDTO("pin", "pinpwd123$", "pin@mail.ru");
        empToSave.setUser(userToSave);
        empToSave.setDepartment(depart2);
        empToSave.setEmployeeRoles(roles3);
        savedEmp = empService.createEmployee(empToSave);
        if (savedEmp.getId() != null) {
            listIDs.add(savedEmp.getId());
            updateRoles(allRoles, savedEmp.getEmployeeRoles());
        }
        assertEquals(3, listIDs.size());
    }

    @Test
    public void findEmployeesInDepartment() {
        List<EmployeeDTO> employees = empService.getEmployeesToDepartment("Java Department");
        assertEquals(2, employees.size());
        assertEquals("Krosh", employees.get(0).getName());
        assertEquals("Nyusha", employees.get(1).getName());
    }

    @Test
    public void findAllEmployeesWithPages() {
        // first page
        List<EmployeeDTO> employees = empService.getAllEmployees();
        assertEquals(2, employees.size());
        assertEquals("Admin", employees.get(0).getName());
        assertEquals(adminLogin, employees.get(0).getUser().getLogin());
        assertEquals("Krosh", employees.get(1).getName());
        assertEquals("krosh", employees.get(1).getUser().getLogin());

        // second page
        employees = empService.getAllEmployees(1);
        assertEquals(2, employees.size());
        assertEquals("Nyusha", employees.get(0).getName());
        assertEquals("nyusha@gmail.com", employees.get(0).getUser().getEmail());
        assertTrue(encoder.matches("nyushapwd", employees.get(0).getUser().getPasswordHash()));
        assertEquals("Pin", employees.get(1).getName());
    }

    @Test
    public void updateEmployee() {
        Long empId = listIDs.get(2);
        EmployeeDTO empToSave = empService.getEmployeeById(empId);
        if (empToSave != null) {
            assertEquals("Pin", empToSave.getName());
            // updating fields
            empToSave.setSurname("Penguin");
            empToSave.setEmploymentDate(LocalDate.of(2023, 1, 1));
            // 1 new role
            empToSave.getEmployeeRoles().add(new RoleDTO("Administrator"));
            // new password
            UserDTO userToSave = empToSave.getUser();
            userToSave.setPassword("newpinpassword");
            empToSave.setUser(userToSave);
            EmployeeDTO savedEmployee = empService.updateEmployee(empToSave);
            assertEquals("Penguin", savedEmployee.getSurname());
            assertTrue(encoder.matches("newpinpassword", savedEmployee.getUser().getPasswordHash()));
            assertEquals(2, savedEmployee.getEmployeeRoles().size());
        }
    }

    @Test
    public void updateEmployeeDuplicateByNameException() {
        Long empId = listIDs.get(2);
        EmployeeDTO empToSave = empService.getEmployeeById(empId);
        if (empToSave != null) {
            assertEquals("Pin", empToSave.getName());
            empToSave.setName("Nyusha");
            empToSave.setSurname("Smesharik");
            assertThrows(EntityDuplicateException.class, () ->
                empService.updateEmployee(empToSave),
                "EntityDuplicateException was expected.");
        }
    }

    @AfterEach
    public void tearDown() {
        for (Long id : listIDs) {
            assertTrue(empService.deleteEmployeeById(id));
        }
        listIDs.clear();
        // delete all users except "admin"
        userRepository.deleteByLoginNot(adminLogin);
        departRepository.deleteByNameNot(defaultDepartment);
        roleRepository.deleteAllExceptForSecurity();
    }
}
