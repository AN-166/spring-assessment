package com.assement.customerbatch.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.assement.customerbatch.security.JwtAuthenticationFilter;

import java.util.Collections;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // Replace with DB-backed user details if needed
        return new InMemoryUserDetailsManager(
                User.withUsername("admin").password("{noop}123456").roles("ADMIN").build()
        );
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService) {
        return new ProviderManager(Collections.singletonList(
                new org.springframework.security.authentication.dao.DaoAuthenticationProvider() {{
                    setUserDetailsService(userDetailsService);
                }}
        ));
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeHttpRequests()
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/api/customers/search").authenticated()
                .requestMatchers("/api/customers/update/**").authenticated()
                .anyRequest().authenticated()
            .and()
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
