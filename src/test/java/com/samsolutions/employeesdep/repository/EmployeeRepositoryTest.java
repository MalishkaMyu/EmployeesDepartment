package com.samsolutions.employeesdep.repository;

import com.samsolutions.employeesdep.TestDataHelper;
import com.samsolutions.employeesdep.model.dao.JpaRoleDao;
import com.samsolutions.employeesdep.model.entities.Department;
import com.samsolutions.employeesdep.model.entities.Employee;
import com.samsolutions.employeesdep.model.entities.Role;
import com.samsolutions.employeesdep.model.entities.User;
import com.samsolutions.employeesdep.model.enums.Gender;
import com.samsolutions.employeesdep.model.repository.DepartmentRepository;
import com.samsolutions.employeesdep.model.repository.EmployeeRepository;
import com.samsolutions.employeesdep.model.repository.UserRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EmployeeRepositoryTest {

    @Value("classpath:insert_emp.sql")
    private Resource insertEmployeesSQLScript;
    @Value("classpath:delete_emp.sql")
    private Resource deleteEmployeesSQLScript;

    @Autowired
    private EmployeeRepository repository;

    @Autowired
    private DepartmentRepository departRepository;

    @Autowired
    private JpaRoleDao roleRepository;

    @Autowired
    private UserRepository userRepository;

    private Employee emp1;

    @Autowired
    private TestDataHelper testDataHelper;

    @BeforeAll
    public void setupClass() throws IOException {
        testDataHelper.executeSqlScript(insertEmployeesSQLScript);
    }

    @BeforeEach
    public void setup() {
        // creating new employee
        emp1 = new Employee("Krosh", "Smesharik", Gender.MALE,
                LocalDate.of(2010, 10, 11),
                LocalDate.of(2020, 7, 20));
        // reading department and assign it to the employee
        if (departRepository.existsById(2L)) {
            Department depart1 = departRepository.findById(2L).orElse(null);
            emp1.setDepartment(depart1);
        }
        // reading user and assign it to employee
        if (userRepository.existsByLogin("krosh")) {
            User userKrosh = userRepository.findByLogin("krosh").orElse(null);
            emp1.setUser(userKrosh);
        }
        // reading all roles from database, use first 3 for our employee
        List<Role> listRoles = roleRepository.findAll();
        Set<Role> roles = Set.of(listRoles.get(0), listRoles.get(1), listRoles.get(2));
        emp1.setEmployeeRoles(roles);
        // saving employee with 1 department and 3 roles
        repository.save(emp1);
    }

    @Test
    public void testReadOne() {
        assertTrue(repository.existsById(emp1.getId()));
        if (repository.findById(emp1.getId()).isPresent()) {
            Employee readEmp = repository.findById(emp1.getId()).get();
            assertEquals("Krosh", readEmp.getName());
            assertEquals("Smesharik", readEmp.getSurname());
            assertNull(readEmp.getPassNumber());
            assertEquals("Java Department", readEmp.getDepartment().getName());
            assertEquals(3, repository.countRolesOfEmployee(readEmp.getId()));
        }
    }

    @Test
    public void testCreateEmpInDepartmentDelete() {
        // creating the new employee with the existing department
        Employee empNew = new Employee("Nyusha", "Smesharik", Gender.FEMALE,
                LocalDate.of(2010, 1, 13), null);
        if (departRepository.existsById(2L)) {
            Department depart1 = departRepository.findById(2L).orElse(null);
            empNew.setDepartment(depart1);
        }
        // reading user and assign it to employee
        if (userRepository.existsByLogin("nyusha")) {
            User userNyusha = userRepository.findByLogin("nyusha").orElse(null);
            empNew.setUser(userNyusha);
        }
        // reading all roles from database, use 3 for our employee
        List<Role> listRoles = roleRepository.findAll();
        Set<Role> roles = Set.of(listRoles.get(0), listRoles.get(2), listRoles.get(3));
        empNew.setEmployeeRoles(roles);
        Employee savedEmp = repository.save(empNew);

        // check
        assertEquals("Java Department", savedEmp.getDepartment().getName());
        assertEquals(3, savedEmp.getEmployeeRoles().size());

        // delete the new employee and check
        repository.delete(savedEmp);
        assertEquals(2, departRepository.findAll().size());
        assertEquals(4, userRepository.findAll().size());
        assertEquals(4, roleRepository.findAll().size());
        assertEquals(1, repository.findAll().size());
    }

    @Test
    public void testReadOneUpdateNewDepartment() {
        List<Role> listRoles = roleRepository.findAll();
        //reading of the existing employee "Krosh Smesharik"
        Employee readUpdEmp = repository.findById(emp1.getId()).get();
        assertEquals("Smesharik", readUpdEmp.getSurname());
        // update name and pass number
        readUpdEmp.setName("Losyash");
        readUpdEmp.setPassNumber("SH123456");
        // setting the new roles - 2 of 3
        Set<Role> roles = Set.of(listRoles.get(1), listRoles.get(2));
        readUpdEmp.setEmployeeRoles(roles);
        // reading another Department and assign the employee this department
        if (departRepository.existsById(3L)) {
            Department depart2 = departRepository.findById(3L).orElse(null);
            readUpdEmp.setDepartment(depart2);
        }
        // reading user and assign it to employee
        if (userRepository.existsByLogin("losyash")) {
            User userLosyash = userRepository.findByLogin("losyash").orElse(null);
            readUpdEmp.setUser(userLosyash);
        }
        // saving all changes in the DB
        repository.save(readUpdEmp);
        // rereading of the employee und check
        if (repository.findById(emp1.getId()).isPresent()) {
            readUpdEmp = repository.findById(emp1.getId()).get();
            assertEquals("Losyash", readUpdEmp.getName());
            assertEquals("Smesharik", readUpdEmp.getSurname());
            assertEquals("SH123456", readUpdEmp.getPassNumber());
            assertEquals(2, repository.countRolesOfEmployee(readUpdEmp.getId()));
            assertEquals("C# Department", readUpdEmp.getDepartment().getName());
            assertEquals("losyash", readUpdEmp.getUser().getLogin());
            // return all back
            readUpdEmp.setName("Krosh");
            roles = Set.of(listRoles.get(0), listRoles.get(1), listRoles.get(2));
            readUpdEmp.setEmployeeRoles(roles);
            Department depart1 = departRepository.findById(2L).orElse(null);
            readUpdEmp.setDepartment(depart1);
            if (userRepository.existsByLogin("krosh")) {
                User userKrosh = userRepository.findByLogin("krosh").orElse(null);
                readUpdEmp.setUser(userKrosh);
            }
            repository.save(readUpdEmp);
            assertEquals("Java Department", readUpdEmp.getDepartment().getName());
            assertEquals("krosh", readUpdEmp.getUser().getLogin());
        }
    }

    @Test
    public void testFindByNameAndSurname() {
        Employee readEmp;
        if (repository.existsByNameAndSurname("Krosh", "Smesharik")) {
            readEmp = repository.findByNameAndSurname("Krosh", "Smesharik");
            assertEquals("Krosh", readEmp.getName());
            assertEquals("Smesharik", readEmp.getSurname());
            assertEquals("Java Department", readEmp.getDepartment().getName());
        }
        readEmp = repository.findByNameAndSurname("Nyusha", "Smesharik");
        assertNull(readEmp);
    }

    @Test
    public void testFindInDepartment() {
        // new Employee "Nyusha Smesharik" in the "Java Department"
        LocalDate employmentDate = null;
        Employee empNew = new Employee("Nyusha", "Smesharik", Gender.FEMALE,
                LocalDate.of(2010, 1, 13), employmentDate);
        if (departRepository.existsById(2L)) {
            Department depart1 = departRepository.findById(2L).orElse(null);
            empNew.setDepartment(depart1);
        }
        // 1 role
        Role role = roleRepository.find(5L).orElse(null);
        if (role != null) {
            Set<Role> rolesToSave = Set.of(role);
            empNew.setEmployeeRoles(rolesToSave);
        }
        // reading user and assign it to employee
        if (userRepository.existsByLogin("nyusha")) {
            User userNyusha = userRepository.findByLogin("nyusha").orElse(null);
            empNew.setUser(userNyusha);
        }
        // saving the new employee
        Employee savedEmp = repository.save(empNew);

        // There are 2 employees in the "Java Department" now
        List<Employee> empInDepart = repository.findInDepartment("Java Department");
        assertEquals(2, empInDepart.size());

        // delete the new employee
        repository.delete(savedEmp);
    }

    @AfterEach
    public void tearDown() {
        repository.delete(emp1);
        assertEquals(0, repository.findAll().size());
        assertEquals(2, departRepository.findAll().size());
        assertEquals(4, roleRepository.findAll().size());
    }

    @AfterAll
    public void tearDownClass() throws IOException {
        testDataHelper.executeSqlScript(deleteEmployeesSQLScript);
    }
}
