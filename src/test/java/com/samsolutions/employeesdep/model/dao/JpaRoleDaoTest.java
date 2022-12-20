package com.samsolutions.employeesdep.model.dao;

import com.samsolutions.employeesdep.model.entities.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class JpaRoleDaoTest {
    @Autowired
    private JpaRoleDao jpaRoleDao;
    private Long id;

    @BeforeEach
    public void setup() {
        Role role = new Role();
        role.setRole("Programmer");
        this.id = jpaRoleDao.save(role);
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
        jpaRoleDao.save(role2);

        // reading all roles
        List<Role> readRoles = jpaRoleDao.findAll();
        assertEquals(2, readRoles.size());
        assertEquals("Programmer", readRoles.get(0).getRole());
        assertEquals("ProjectManager", readRoles.get(1).getRole());

        // deleting role 2
        jpaRoleDao.deleteById(readRoles.get(1).getId());
        readRoles = jpaRoleDao.findAll();
        assertEquals(1, readRoles.size());
        assertEquals("Programmer", readRoles.get(0).getRole());
    }

    @AfterEach
    public void tearDown() {
        jpaRoleDao.deleteById(this.id);
        assertThat(jpaRoleDao.findAll(), is(empty()));
    }
}
