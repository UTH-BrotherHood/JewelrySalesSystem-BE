package com.example.JewelrySalesSystem.configuration;

import com.example.JewelrySalesSystem.constant.PredefinedRole;
import com.example.JewelrySalesSystem.entity.Employee;
import com.example.JewelrySalesSystem.entity.Role;
import com.example.JewelrySalesSystem.repository.CustomerRepository;
import com.example.JewelrySalesSystem.repository.EmployeeRepository;
import com.example.JewelrySalesSystem.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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

    @NonFinal
    static final String ADMIN_USER_NAME = "admin";

    @NonFinal
    static final String ADMIN_PASSWORD = "admin";

    @Bean
    @ConditionalOnProperty(
            prefix = "spring",
            value = "datasource.driver-class-name",
            havingValue = "com.mysql.cj.jdbc.Driver")
//    @ConditionalOnProperty(
//            prefix = "spring",
//            value = "datasource.url",
//            havingValue = "jdbc:h2:mem:testdb_app;MODE=MYSQL"
//    )
    ApplicationRunner applicationRunner(CustomerRepository customerRepository, EmployeeRepository employeeRepository, RoleRepository roleRepository) {
        log.info("Initializing application.....");
        return args -> {
            if (employeeRepository.findByUsername(ADMIN_USER_NAME).isEmpty()) {
                // Create and save Employee role
                roleRepository.save(Role.builder()
                        .name(PredefinedRole.EMPLOYEE_ROLE)
                        .description("Employee role")
                        .build());

                // Create and save Admin role
                Role adminRole = roleRepository.save(Role.builder()
                        .name(PredefinedRole.ADMIN_ROLE)
                        .description("Admin role")
                        .build());

                // Create and save Customer role
                roleRepository.save(Role.builder()
                        .name(PredefinedRole.CUSTOMER_ROLE)
                        .description("Customer role")
                        .build());

                var roles = new HashSet<Role>();
                roles.add(adminRole);

                // Create and save admin user with Admin role
                Employee employee = Employee.builder()
                        .username(ADMIN_USER_NAME)
                        .password(passwordEncoder.encode(ADMIN_PASSWORD))
                        .name("Admin Name")
                        .phoneNumber("123")
                        .roles(roles)
                        .build();

                employeeRepository.save(employee);
                log.warn("Admin user has been created with default password: admin, please change it.");
            }
            log.info("Application initialization completed .....");
        };
    }
}
