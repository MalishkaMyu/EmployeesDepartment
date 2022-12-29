package com.samsolutions.employeesdep.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.samsolutions.employeesdep.model.dao.JpaRoleDao;
import com.samsolutions.employeesdep.model.entities.Department;
import com.samsolutions.employeesdep.model.entities.Employee;
import com.samsolutions.employeesdep.model.entities.Role;
import com.samsolutions.employeesdep.model.enums.Gender;
import com.samsolutions.employeesdep.model.repository.DepartmentRepository;
import com.samsolutions.employeesdep.model.repository.EmployeeRepository;

@SpringBootTest
public class EmployeeRepositoryTest {
    @Autowired
    private EmployeeRepository repository;

    @Autowired
    private DepartmentRepository departRepository;

    @Autowired
    private JpaRoleDao roleRepository;

    private Employee emp1;
    private Department depart1;
    private final Set<Role> roles = new HashSet<>();

    @BeforeEach
    public void setup() {
        // saving 1 new department
        depart1 = new Department("Java Department");
        departRepository.save(depart1);
        // creating new employee
        emp1 = new Employee("Krosh", "Smesharik", Gender.MALE,
                LocalDate.of(2010, 10, 11),
                LocalDate.of(2020, 7, 20));
        emp1.setDepartment(depart1);
        // creating and saving 3 new roles
        String[] roleNames = {"Programmer", "Tester", "ProjectManager"};
        for (String roleName : roleNames) {
            Role role = new Role(roleName);
            roleRepository.save(role);
            roles.add(role);
        }
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
        empNew.setDepartment(depart1);
        // creating and saving roles for the employee - two existing roles and one new
        Set<Role> subsetRoles = new HashSet<>(2);
        for (int i = 0; i < roles.size() - 1; i++) {
            Role role = (Role) roles.toArray()[i];
            subsetRoles.add(role);
        }
        Role newRole = new Role("Cleaning");
        roleRepository.save(newRole);
        subsetRoles.add(newRole);
        empNew.setEmployeeRoles(subsetRoles);
        Employee savedEmp = repository.save(empNew);
        // check
        assertEquals("Java Department", savedEmp.getDepartment().getName());
        assertEquals(3, savedEmp.getEmployeeRoles().size());
        assertEquals(1, departRepository.findAll().size());
        assertEquals(2, repository.findAll().size());
        // delete the new employee and check
        repository.delete(savedEmp);
        assertEquals(1, departRepository.findAll().size());
        assertEquals(4, roleRepository.findAll().size());
        assertEquals(1, repository.findAll().size());
        roleRepository.deleteById(newRole.getId());
    }

    @Test
    public void testReadOneUpdateNewDepartment() {
        //reading of the existing employee "Krosh Smescharik"
        Employee readUpdEmp = repository.findById(emp1.getId()).get();
        assertEquals("Smesharik", readUpdEmp.getSurname());
        // update name and pass number
        readUpdEmp.setName("Losyash");
        readUpdEmp.setPassNumber("SH123456");
        // setting the new roles - 2 old
        Set<Role> empRoles = new HashSet<>();
        for (int i = 0; i < roles.size() - 1; i++) {
            Role role = (Role) roles.toArray()[i];
            empRoles.add(role);
        }
        readUpdEmp.setEmployeeRoles(empRoles);
        // create new Department and assign the employee the new department
        Department departToSave = new Department("C# Department");
        departRepository.save(departToSave);
        readUpdEmp.setDepartment(departToSave);
        // saving all changes in the DB
        repository.save(readUpdEmp);
        // rereading of the employee und check
        if (repository.findById(emp1.getId()).isPresent()) {
            readUpdEmp = repository.findById(emp1.getId()).get();
            assertEquals("Losyash", readUpdEmp.getName());
            assertEquals("Smesharik", readUpdEmp.getSurname());
            assertEquals("SH123456", readUpdEmp.getPassNumber());
            assertEquals(2, repository.countRolesOfEmployee(readUpdEmp.getId()));
            assertEquals(3, roleRepository.findAll().size());
            assertEquals(2, departRepository.findAll().size());
            assertEquals("C# Department", readUpdEmp.getDepartment().getName());
            // return all back
            readUpdEmp.setName("Krosh");
            readUpdEmp.setEmployeeRoles(roles);
            departToSave = readUpdEmp.getDepartment();
            readUpdEmp.setDepartment(depart1);
            repository.save(readUpdEmp);
            assertEquals("Java Department", readUpdEmp.getDepartment().getName());
        }
        departRepository.delete(departToSave);
        assertEquals(1, departRepository.findAll().size());
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
        empNew.setDepartment(depart1);
        // 1 role
        Role role = new Role("Cleaning");
        roleRepository.save(role);
        Set<Role> rolesToSave = new HashSet<>(1);
        rolesToSave.add(role);
        empNew.setEmployeeRoles(rolesToSave);
        // saving the new employee. There are 2 employees in the "Java Department" now
        Employee savedEmp = repository.save(empNew);
        List<Employee> empInDepart = repository.findInDepartment("Java Department");
        assertEquals(2, empInDepart.size());
        // delete the new employee
        repository.delete(savedEmp);
        roleRepository.deleteById(role.getId());
    }

    @AfterEach
    public void tearDown() {
        repository.delete(emp1);
        assertEquals(0, repository.findAll().size());
        assertEquals(1, departRepository.findAll().size());
        assertEquals(3, roleRepository.findAll().size());
        departRepository.delete(depart1);
        assertEquals(0, departRepository.findAll().size());
        int cntRoles = roleRepository.deleteAll();
        roles.clear();
        assertEquals(0, roleRepository.findAll().size());
    }
}
