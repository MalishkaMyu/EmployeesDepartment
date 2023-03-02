package com.samsolutions.employeesdep.repository;

import com.samsolutions.employeesdep.model.entities.Department;
import com.samsolutions.employeesdep.model.repository.DepartmentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
public class DepartmentRepositoryTest {
    @Autowired
    private DepartmentRepository repository;

    private Department depart1;
    private Department depart2;

    @BeforeEach
    public void setup() {
        depart1 = new Department("Java Department");
        repository.save(depart1);
        assertEquals("Java Department", depart1.getName());
        depart2 = new Department(".NET Department");
        repository.save(depart2);
        assertEquals(".NET Department", depart2.getName());
    }

    @Test
    void testFindAll() {
        List<Department> departs = repository.findAll();
        assertEquals(3, departs.size());
        assertNotEquals(depart1.getId(), depart2.getId());
    }

    @Test
    void testGetByName() {
        Department depart = repository.findByName("Java Department");
        assertEquals("Java Department", depart.getName());
    }

    @Test
    void testUpdate() {
        depart1.setName("PHP Department");
        repository.save(depart1);
        assertEquals("PHP Department", depart1.getName());
        assertNotEquals("Java Department", depart1.getName());
    }

    @AfterEach
    public void tearDown() {
        repository.deleteById(depart1.getId());
        repository.delete(depart2);
        assertEquals(1, repository.findAll().size());
    }
}
