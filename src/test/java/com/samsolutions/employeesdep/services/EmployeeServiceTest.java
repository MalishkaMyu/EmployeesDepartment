package com.samsolutions.employeesdep.services;

import com.samsolutions.employeesdep.model.dao.JpaRoleDao;
import com.samsolutions.employeesdep.model.dto.DepartmentDTO;
import com.samsolutions.employeesdep.model.dto.EmployeeDTO;
import com.samsolutions.employeesdep.model.dto.RoleDTO;
import com.samsolutions.employeesdep.model.enums.Gender;
import com.samsolutions.employeesdep.model.repository.DepartmentRepository;
import com.samsolutions.employeesdep.model.services.EmployeeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class EmployeeServiceTest {
    @Autowired
    private EmployeeService empService;

    @Autowired
    private DepartmentRepository departRepository;

    @Autowired
    private JpaRoleDao roleRepository;

    private final List<Long> listIDs = new ArrayList<>();
    private final static RoleDTO[] allRoles = {new RoleDTO("Java Programmer"), new RoleDTO(".NET programmer"),
            new RoleDTO("Tester"), new RoleDTO("Project manager"), new RoleDTO("coffee drinker")};
    private static DepartmentDTO depart1 = new DepartmentDTO("Java Department");
    private static DepartmentDTO depart2 = new DepartmentDTO(".NET Department");

    private void updateRoles(Set<RoleDTO> savedRoles) {
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
        // creating and saving employee 1
        Set<RoleDTO> roles1 = Set.of(allRoles[0], allRoles[2], allRoles[4]); // Java-Roles
        empToSave = new EmployeeDTO("Krosh", "Smesharik", Gender.MALE,
                LocalDate.of(2010, 10, 11),
                LocalDate.of(2020, 7, 20));
        empToSave.setDepartment(depart1);
        empToSave.setEmployeeRoles(roles1);
        savedEmp = empService.createEmployee(empToSave);
        if (savedEmp.getId() != null) {
            listIDs.add(savedEmp.getId());
            depart1 = savedEmp.getDepartment();
            updateRoles(savedEmp.getEmployeeRoles());
        }
        // creating and saving employee 2
        Set<RoleDTO> roles2 = Set.of(allRoles[3], allRoles[4]); // PM-roles
        empToSave = new EmployeeDTO("Nyusha", "Smesharik", Gender.FEMALE,
                LocalDate.of(2010, 1, 13), null);
        empToSave.setDepartment(depart1);
        empToSave.setEmployeeRoles(roles2);
        savedEmp = empService.createEmployee(empToSave);
        if (savedEmp.getId() != null) {
            listIDs.add(savedEmp.getId());
            updateRoles(savedEmp.getEmployeeRoles());
        }
        // creating and saving employee 3
        Set<RoleDTO> roles3 = Set.of(allRoles[1]); // .NET roles
        empToSave = new EmployeeDTO("Pin", "Smesharik", Gender.MALE,
                LocalDate.of(2008, 8, 21),
                LocalDate.of(2020, 3, 3));
        empToSave.setDepartment(depart2);
        empToSave.setEmployeeRoles(roles3);
        savedEmp = empService.createEmployee(empToSave);
        if (savedEmp.getId() != null) {
            listIDs.add(savedEmp.getId());
            depart2 = savedEmp.getDepartment();
            updateRoles(savedEmp.getEmployeeRoles());
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
    public void findAllEmployees() {
        List<EmployeeDTO> employees = empService.getEmployeesToDepartment("");
        assertEquals(3, employees.size());
        assertEquals("Krosh", employees.get(0).getName());
        assertEquals("Nyusha", employees.get(1).getName());
        assertEquals("Pin", employees.get(2).getName());
    }

    @AfterEach
    public void tearDown() {
        for (Long id : listIDs)
            empService.deleteEmployeeById(id);
        listIDs.clear();
        departRepository.deleteAll();
    }
}
