package com.example.JewelrySalesSystem.configuration;

import com.example.JewelrySalesSystem.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private static final String[] PUBLIC_ENDPOINTS = {
            "/auth/sign-in",
            "/auth/log-out",
            "/employees/myInfo",
            "/customers",
            "/roles",
            "/permissions",
            "/products",
            "/products/{productId}",
            "/sales-orders",
            "/statistics",
            "/promotions/{promotionId}",
            "/return-policy",
            "/categories",
//            "/h2-console/**"
    };

    private static final String[] ADMIN_ENDPOINTS = {
            "/categories",
            "/categories/{categoryId}",
            "/employees",
            "/employees/{employeeId}",
            "/permissions",
            "/roles",
            "/promotions",
            "/statistics"
    };

    private static final String[] ADMIN_AND_EMPLOYEE_ENDPOINTS = {
            "/sales-orders",
            "/sales-orders/customer/{customerId}",
            "/sales-orders/employee/{employeeId}",
            "/sales-orders/{orderId}",
            "/sales-orders/{orderId}/details"
    };

   
    @Autowired
    private CustomJwtDecoder customJwtDecoder;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(request ->
                request
                        .requestMatchers(HttpMethod.POST, "/employees").permitAll()
                        .requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.GET, PUBLIC_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.GET, ADMIN_ENDPOINTS).hasRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.POST, ADMIN_ENDPOINTS).hasRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.PUT, ADMIN_ENDPOINTS).hasRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, ADMIN_ENDPOINTS).hasRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, ADMIN_AND_EMPLOYEE_ENDPOINTS).hasAnyRole(Role.ADMIN.name(), Role.EMPLOYEE.name())
                        .requestMatchers(HttpMethod.POST, ADMIN_AND_EMPLOYEE_ENDPOINTS).hasAnyRole(Role.ADMIN.name(), Role.EMPLOYEE.name())
                        .requestMatchers(HttpMethod.PUT, ADMIN_AND_EMPLOYEE_ENDPOINTS).hasAnyRole(Role.ADMIN.name(), Role.EMPLOYEE.name())
                        .requestMatchers(HttpMethod.DELETE, ADMIN_AND_EMPLOYEE_ENDPOINTS).hasRole(Role.ADMIN.name())
                        .anyRequest().authenticated()
        );

        httpSecurity.oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwtConfigurer ->
                                jwtConfigurer.decoder(customJwtDecoder)
                                        .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
        );
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
//        httpSecurity.headers(httpSecurityHeadersConfigurer -> {
//            httpSecurityHeadersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable);
//        });

        return httpSecurity.build();
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }


    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}
