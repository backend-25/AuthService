//package com.example.auth_related.Configs;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//public class ConfigRelated
//    {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
//    {
//        http.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll()
//                ).csrf(csrf -> csrf.disable()); // Disable CSRF protection
//
//        return http.build();
//    }
//}
