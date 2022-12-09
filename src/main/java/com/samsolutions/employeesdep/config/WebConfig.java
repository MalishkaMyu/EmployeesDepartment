package com.samsolutions.employeesdep.config;

import com.samsolutions.employeesdep.model.converters.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new EmployeeDTOToEntityConverter());
        registry.addConverter(new EmployeeEntityToDTOConverter());
        registry.addConverter(new DepartmentDTOToEntityConverter());
        registry.addConverter(new DepartmentEntityToDTOConverter());
        registry.addConverter(new RoleDTOToEntityConverter());
        registry.addConverter(new RoleEntityToDTOConverter());
    }
}
