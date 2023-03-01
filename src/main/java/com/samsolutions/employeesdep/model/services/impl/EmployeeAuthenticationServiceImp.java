package com.samsolutions.employeesdep.model.services.impl;

import com.samsolutions.employeesdep.exception.EntityNotFoundException;
import com.samsolutions.employeesdep.model.converters.EmployeeEntityToDTOConverter;
import com.samsolutions.employeesdep.model.dto.EmployeeDTO;
import com.samsolutions.employeesdep.model.dto.RoleDTO;
import com.samsolutions.employeesdep.model.entities.Employee;
import com.samsolutions.employeesdep.model.repository.EmployeeRepository;
import com.samsolutions.employeesdep.model.services.EmployeeAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EmployeeAuthenticationServiceImp implements EmployeeAuthenticationService {
    @Autowired
    EmployeeRepository empRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        EmployeeDTO readEmployee = findByUserLogin(login);
        if (readEmployee == null) {
            throw new UsernameNotFoundException("User " + login + " is not found!");
        }

        return new org.springframework.security.core.userdetails.User(
                readEmployee.getUser().getLogin(),
                readEmployee.getUser().getPasswordHash(),
                mapRolesToAuthorities(readEmployee.getEmployeeRoles())
        );
    }

    @Transactional
    public EmployeeDTO findByUserLogin(String login) {
        Employee readEmployee = empRepository.findByUserLogin(login).orElse(null);
        if (readEmployee == null) {
            throw new EntityNotFoundException("There is no employee user login " + login,
                    Employee.class);
        }
        return new EmployeeEntityToDTOConverter().convert(readEmployee);
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Set<RoleDTO> roles) {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getRole())).collect(Collectors.toList());
    }
}
