package com.example.JewelrySalesSystem.configuration;

import com.example.JewelrySalesSystem.entity.Employee;
import com.example.JewelrySalesSystem.enums.Role;
import com.example.JewelrySalesSystem.repository.EmployeeRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(EmployeeRepository employeeRepository){
        return args -> {
            if (employeeRepository.findByUsername("admin").isEmpty()){
                var roles = new HashSet<String>();
                roles.add(Role.ADMIN.name());

                Employee user = Employee.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .phoneNumber("012344566")
                        .name("admin")
//                         .roles(roles)
                        .build();

                employeeRepository.save(user);
                log.warn("admin user has been created with default password: admin, please change it");
            }
        };
    }
}
