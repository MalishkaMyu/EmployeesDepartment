package com.samsolutions.employeesdep.model.services.impl;

import com.samsolutions.employeesdep.exception.EntityDuplicateException;
import com.samsolutions.employeesdep.exception.EntityNotFoundException;
import com.samsolutions.employeesdep.model.converters.EmployeeDTOToEntityConverter;
import com.samsolutions.employeesdep.model.converters.EmployeeEntityToDTOConverter;
import com.samsolutions.employeesdep.model.converters.RoleDTOToEntityConverter;
import com.samsolutions.employeesdep.model.converters.UserDTOToEntityConverter;
import com.samsolutions.employeesdep.model.dao.JpaRoleDao;
import com.samsolutions.employeesdep.model.dto.EmployeeDTO;
import com.samsolutions.employeesdep.model.dto.RoleDTO;
import com.samsolutions.employeesdep.model.dto.UserDTO;
import com.samsolutions.employeesdep.model.entities.Department;
import com.samsolutions.employeesdep.model.entities.Employee;
import com.samsolutions.employeesdep.model.entities.Role;
import com.samsolutions.employeesdep.model.entities.User;
import com.samsolutions.employeesdep.model.repository.DepartmentRepository;
import com.samsolutions.employeesdep.model.repository.EmployeeRepository;
import com.samsolutions.employeesdep.model.services.EmployeeService;
import com.samsolutions.employeesdep.model.services.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Value("${employees.per.page}")
    private int employeesPerPage;

    @Autowired
    private EmployeeRepository empRepository;

    @Autowired
    private DepartmentRepository departRepository;

    @Autowired
    private JpaRoleDao roleRepository;

    @Autowired
    private UserService userService;

    public List<EmployeeDTO> getEmployeesToDepartmentCommon(String departName, int page, int pageSize) {
        Pageable paging = PageRequest.of(page, pageSize);
        EmployeeEntityToDTOConverter employeeEntityToDTOConverter = new EmployeeEntityToDTOConverter();
        if (StringUtils.isBlank(departName)) {
            return null;
        } else {
            return empRepository.findInDepartment(departName, paging).stream() //stream from list
                    .map(employeeEntityToDTOConverter::convert) //convert every element
                    .collect(Collectors.toList());
        }
    }

    @Override
    @Transactional
    public List<EmployeeDTO> getEmployeesToDepartment(String departName) {
        return getEmployeesToDepartmentCommon(departName, 0, employeesPerPage);
    }

    @Override
    @Transactional
    public List<EmployeeDTO> getEmployeesToDepartment(String departName, int page) {
        return getEmployeesToDepartmentCommon(departName, page, employeesPerPage);
    }

    @Override
    @Transactional
    public EmployeeDTO getEmployeeById(Long employeeId) {
        if (empRepository.findById(employeeId).isPresent()) {
            Employee readEmp = empRepository.findById(employeeId).get();
            return new EmployeeEntityToDTOConverter().convert(readEmp);
        }
        else
            throw new EntityNotFoundException("There is no Employee with ID " + employeeId,
                    Employee.class.getSimpleName());

    }

    @Override
    @Transactional
    public List<EmployeeDTO> getAllEmployees() {
        Pageable paging = PageRequest.of(0, employeesPerPage);
        EmployeeEntityToDTOConverter employeeEntityToDTOConverter = new EmployeeEntityToDTOConverter();
        return empRepository.findAll(paging).stream() //stream from list
                .map(employeeEntityToDTOConverter::convert) //convert every element
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<EmployeeDTO> getAllEmployees(int page) {
        Pageable paging = PageRequest.of(page, employeesPerPage);
        EmployeeEntityToDTOConverter employeeEntityToDTOConverter = new EmployeeEntityToDTOConverter();
        return empRepository.findAll(paging).stream() //stream from list
                .map(employeeEntityToDTOConverter::convert) //convert every element
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EmployeeDTO createEmployee(EmployeeDTO employeeToSaveDTO) {
        Employee employeeToSave = new EmployeeDTOToEntityConverter().convert(employeeToSaveDTO);
        if (employeeToSave == null)
            return null;

        //check whether employee already exists
        if (employeeToSave.getId() != null && empRepository.existsById(employeeToSave.getId())) {
            // employee with ID is already exists
            throw new EntityDuplicateException(
                    "The employee with ID " + employeeToSave.getId() + " already exists and can not be created.",
                    Employee.class.getSimpleName());
        }
        // check whether employee with the name already exists
        if (empRepository.existsByNameAndSurname(employeeToSave.getName(), employeeToSave.getSurname())) {
            // employee is already exists
            throw new EntityDuplicateException(
                    "The employee " + employeeToSave.getName() + " " + employeeToSave.getSurname() +
                    " is already registered. Please choose another name.",
                    Employee.class.getSimpleName());
        }

        // saving department if new
        Department departToSave;
        if (employeeToSaveDTO.getDepartment().getId() == null) {
            departToSave = new Department(employeeToSaveDTO.getDepartment().getName());
            departRepository.save(departToSave);
        } else {
            departToSave = departRepository.getReferenceById(employeeToSaveDTO.getDepartment().getId());
        }
        employeeToSave.setDepartment(departToSave);

        // saving roles if new
        RoleDTOToEntityConverter roleDTOToEntityConverter = new RoleDTOToEntityConverter();
        Set<Role> rolesToSave = new HashSet<>();
        for (RoleDTO roleDTO : employeeToSaveDTO.getEmployeeRoles()) {
            Role roleToSave = roleDTOToEntityConverter.convert(roleDTO);
            if (roleToSave != null) {
                if (roleToSave.getId() == null || roleToSave.getId() == 0) {
                    roleRepository.save(roleToSave);
                }
                rolesToSave.add(roleToSave);
            }
        }
        employeeToSave.setEmployeeRoles(rolesToSave);

        // create new user always over UserService !!
        UserDTO savedUserDTO = userService.createUser(employeeToSaveDTO.getUser());
        employeeToSave.setUser(new UserDTOToEntityConverter().convert(savedUserDTO));

        // saving employee
        empRepository.save(employeeToSave);
        // convert employee entity back to DTO to have all IDs
        return new EmployeeEntityToDTOConverter().convert(employeeToSave);
    }

    @Override
    @Transactional
    public EmployeeDTO updateEmployee(EmployeeDTO employeeToSaveDTO) {
        Employee employeeToSave = new EmployeeDTOToEntityConverter().convert(employeeToSaveDTO);
        if (employeeToSave == null)
            return null;

        //check whether employee doesn't exist
        if (employeeToSave.getId() == null || !empRepository.existsById(employeeToSave.getId())) {
            // employee is not still exists
            throw new EntityNotFoundException("There is no employee with ID " + employeeToSave.getId(),
                    Employee.class.getSimpleName());
        }
        // reading current employee information from database
        Employee existingEmployee = empRepository.findById(employeeToSave.getId()).orElse(null);
        // check whether employee with the new name already exists
        if (empRepository.existsByNameAndSurname(employeeToSave.getName(), employeeToSave.getSurname()) &&
                existingEmployee != null &&
                (!employeeToSave.getName().equals(existingEmployee.getName()) ||
                 !employeeToSave.getSurname().equals(existingEmployee.getSurname()))) {
            // employee with new name is already exists
            throw new EntityDuplicateException(
                    "The employee with the name " + employeeToSave.getName() + " " + employeeToSave.getSurname() +
                            " is already registered. Please choose another name.",
                    Employee.class.getSimpleName());
        }

        // saving department if new
        Department departToSave;
        if (employeeToSaveDTO.getDepartment().getId() == null) {
            departToSave = new Department(employeeToSaveDTO.getDepartment().getName());
            departRepository.save(departToSave);
        } else {
            departToSave = departRepository.getReferenceById(employeeToSaveDTO.getDepartment().getId());
        }
        employeeToSave.setDepartment(departToSave);

        // saving roles if new
        RoleDTOToEntityConverter roleDTOToEntityConverter = new RoleDTOToEntityConverter();
        Set<Role> rolesToSave = new HashSet<>();
        for (RoleDTO roleDTO : employeeToSaveDTO.getEmployeeRoles()) {
            Role roleToSave = roleDTOToEntityConverter.convert(roleDTO);
            if (roleToSave != null) {
                if (roleToSave.getId() == null || roleToSave.getId() == 0) {
                    roleRepository.save(roleToSave);
                }
                rolesToSave.add(roleToSave);
            }
        }
        employeeToSave.setEmployeeRoles(rolesToSave);

        // update user over UserService
        UserDTO savedUserDTO = userService.updateUser(employeeToSaveDTO.getUser());
        employeeToSave.setUser(new UserDTOToEntityConverter().convert(savedUserDTO));

        // saving employee
        empRepository.save(employeeToSave);
        // convert employee entity back to DTO to have all IDs
        return new EmployeeEntityToDTOConverter().convert(employeeToSave);
    }

    @Override
    @Transactional
    public boolean deleteEmployeeById(Long employeeId) {
        if (empRepository.existsById(employeeId)) {
            // reading corresponding user
            User userToDelete = empRepository.getReferenceById(employeeId).getUser();
            // deleting employee
            empRepository.deleteById(employeeId);
            // deleting the corresponding user
            if (!userService.deleteUserById(userToDelete.getId()))
                return false;

            return !empRepository.existsById(employeeId);
        } else
            throw new EntityNotFoundException("There is no Employee with ID " + employeeId + " to delete.",
                    Employee.class.getSimpleName());
    }
}
