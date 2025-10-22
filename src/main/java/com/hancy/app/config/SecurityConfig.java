package com.hancy.app.config;

import com.hancy.app.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

  @Autowired private JwtAuthenticationFilter jwtAuthFilter;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        // Disable CSRF since we're using JWTs (no session-based auth)
        .csrf(csrf -> csrf.disable())

        // Define authorization rules
        .authorizeHttpRequests(
            auth ->
                auth
                    // Allow static resources and main pages
                    .requestMatchers("/", "/blog-app", "/css/**", "/js/**")
                    .permitAll()

                    // Allow login and signup APIs
                    .requestMatchers("/api/users/login", "/api/users/signup")
                    .permitAll()

                    // All other API requests require authentication
                    .requestMatchers("/api/**")
                    .authenticated()

                    // Any other request (e.g. Thymeleaf views) is permitted
                    .anyRequest()
                    .permitAll())

        // Add JWT authentication filter before the default UsernamePasswordAuthenticationFilter
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
