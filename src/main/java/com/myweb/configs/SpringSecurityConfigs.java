/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myweb.configs;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.myweb.utils.GeneralUtils;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

/**
 *
 * @author huaquangdat
 */
@Configuration
@EnableTransactionManagement
@EnableWebSecurity
@ComponentScan(basePackages = {
    "com.myweb.controllers",
    "com.myweb.repositories",
    "com.myweb.services"
})
public class SpringSecurityConfigs {

    @Autowired
    @Qualifier("userDetailsService")
    private UserDetailsService userDetailsService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public HandlerMappingIntrospector mvcHandlerMappingIntrospector() {
        return new HandlerMappingIntrospector();
    }

    @Bean
    public Cloudinary cloudinary() {
        String apiSecret = System.getenv("API_SECRET_CLOUDINARY");
        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dmbvjjg5a",
                "api_key", "463513198463353",
                "api_secret", "HsIK1yhx7av6BeoVqVjKKVceikY",
                "secure", true));
        return cloudinary;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(c -> c.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(requests -> requests
                .requestMatchers("/", "/home").permitAll()
                .requestMatchers("/api/secure/**").authenticated()
                .requestMatchers("/login").permitAll()
                .requestMatchers("/api/**").permitAll()
                .requestMatchers("/admin/**").hasRole(GeneralUtils.Role.ROLE_ADMIN.getShortName())
                .requestMatchers("/api/admin/**").hasRole(GeneralUtils.Role.ROLE_ADMIN.getShortName())
                .requestMatchers(HttpMethod.POST, "/api/secure/applications").hasRole(GeneralUtils.Role.ROLE_CANDIDATE.getShortName())
                .requestMatchers(HttpMethod.PATCH, "/api/secure/applications/update-status").hasRole(GeneralUtils.Role.ROLE_COMPANY.getShortName())
                .requestMatchers(HttpMethod.GET, "/api/secure/applications/**")
                .hasAnyRole(GeneralUtils.Role.ROLE_CANDIDATE.getShortName(), GeneralUtils.Role.ROLE_COMPANY.getShortName())
                .requestMatchers(HttpMethod.POST, "/api/secure/jobs").hasAnyRole(GeneralUtils.Role.ROLE_COMPANY.getShortName())
                .requestMatchers("/candidates/**").hasRole(GeneralUtils.Role.ROLE_ADMIN.getShortName())
                .requestMatchers("/applications/**").hasRole(GeneralUtils.Role.ROLE_ADMIN.getShortName())
                .requestMatchers("/company/**").hasRole(GeneralUtils.Role.ROLE_ADMIN.getShortName())
                .requestMatchers("/job/**").hasRole(GeneralUtils.Role.ROLE_ADMIN.getShortName())
                .requestMatchers("/candidates/**").hasRole(GeneralUtils.Role.ROLE_ADMIN.getShortName())
                .requestMatchers("/companies/**").hasRole(GeneralUtils.Role.ROLE_ADMIN.getShortName())
                .requestMatchers("/report/**").hasRole(GeneralUtils.Role.ROLE_ADMIN.getShortName())
                .requestMatchers("/js/**").permitAll()
                .anyRequest().authenticated())
                .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error=true")
                .permitAll())
                .logout(logout -> logout
                .logoutSuccessUrl("/login")
                .permitAll());

        return http.build();
    }

    @Bean
    @Order(0)
    public StandardServletMultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of("http://localhost:3000")); // frontend origin
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(true); // Nếu dùng cookie/session

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
