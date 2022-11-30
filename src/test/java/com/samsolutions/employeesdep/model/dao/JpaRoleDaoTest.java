package com.samsolutions.employeesdep.model.dao;

import com.samsolutions.employeesdep.model.dao.JpaRoleDao;
import com.samsolutions.employeesdep.model.entities.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

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
            Role readRole = jpaRoleDao.find(this.id).get();
            assertEquals("Programmer", readRole.getRole());
            readRole.setRole("ScrumMaster");
            jpaRoleDao.update(readRole);
            assertEquals("ScrumMaster", readRole.getRole());
            readRole.setRole("Programmer");
            jpaRoleDao.update(readRole);
            assertEquals("Programmer", readRole.getRole());
        }
    }

    @Test
    void testReadAllAndCreateNewRoleAndDelete() {
        Role role2 = new Role();
        role2.setRole("ProjectManager");
        jpaRoleDao.save(role2);
        List<Role> readRoles = jpaRoleDao.findAll();
        assertEquals(2, readRoles.size());
        assertEquals("Programmer", readRoles.get(0).getRole());
        assertEquals("ProjectManager", readRoles.get(1).getRole());
        jpaRoleDao.deleteById(readRoles.get(1).getId());
        readRoles = jpaRoleDao.findAll();
        assertEquals(1, readRoles.size());
        assertEquals("Programmer", readRoles.get(0).getRole());
    }

    @AfterEach
    public void tearDown() {
        jpaRoleDao.deleteById(this.id);
        assertEquals(0, jpaRoleDao.findAll().size());
    }
}
