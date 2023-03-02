package com.samsolutions.employeesdep.model.dao;

import com.samsolutions.employeesdep.model.entities.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
public class JpaRoleDaoTest {
    @Autowired
    private JpaRoleDao jpaRoleDao;

    private Long id;

    @BeforeEach
    public void setup() {
        // main role for tests
        Role role = new Role("Programmer");
        this.id = jpaRoleDao.save(role);
    }

    @Test
    void testReadByRole() {
        if (jpaRoleDao.findByRole("Programmer").isPresent()) {
            Role readRole = jpaRoleDao.findByRole("Programmer").get();
            assertEquals("Programmer", readRole.getRole());
        }
    }

    @Test
    void testReadByRoleNotFound() {
        Role readRole = jpaRoleDao.findByRole("Dancer").orElse(null);
        assertNull(readRole);
    }

    @Test
    void testReadUpdate() {
        if (jpaRoleDao.find(this.id).isPresent()) {
            // reading role
            Role readRole = jpaRoleDao.find(this.id).get();
            assertEquals("Programmer", readRole.getRole());

            // updating role
            readRole.setRole("ScrumMaster");
            jpaRoleDao.update(readRole);
            assertEquals("ScrumMaster", readRole.getRole());

            // return role back
            readRole.setRole("Programmer");
            jpaRoleDao.update(readRole);
            assertEquals("Programmer", readRole.getRole());
        }
    }

    @Test
    void testReadAllAndCreateNewRoleAndDelete() {
        //saving new role 2
        Role role2 = new Role();
        role2.setRole("ProjectManager");
        Long savedID = jpaRoleDao.save(role2);

        // reading all roles
        List<Role> readRoles = jpaRoleDao.findAll();
        assertEquals(4, readRoles.size());
        assertEquals("Programmer", readRoles.get(0).getRole());
        assertEquals("ProjectManager", readRoles.get(1).getRole());

        // deleting role 2
        jpaRoleDao.deleteById(savedID);
        readRoles = jpaRoleDao.findAll();
        assertEquals(3, readRoles.size());
        assertEquals("Programmer", readRoles.get(0).getRole());
    }

    @AfterEach
    public void tearDown() {
        // deleting role "Programmer"
        //jpaRoleDao.deleteById(this.id);
        // deleting all roles except "ROLE_ADMIN" and "ROLE_USER"
        jpaRoleDao.deleteAllExceptForSecurity();
        assertEquals(2, jpaRoleDao.findAll().size());
        //assertThat(jpaRoleDao.findAll(), is(empty()));
    }
}
