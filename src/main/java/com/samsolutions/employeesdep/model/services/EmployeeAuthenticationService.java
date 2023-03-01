package com.samsolutions.employeesdep.model.services;

import com.samsolutions.employeesdep.model.dto.EmployeeDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface EmployeeAuthenticationService extends UserDetailsService {
    EmployeeDTO findByUserLogin(String login);
}
