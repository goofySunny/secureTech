package com.securityproject.secureTech.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private JwtAuthenticationFilter JwtAuthFilter;
    private AuthenticationProvider authenticationProvider;

    @Autowired
    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter, AuthenticationProvider authenticationProvider) {
        this.JwtAuthFilter = jwtAuthFilter;
        this.authenticationProvider = authenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {


        http
            // .csrf()
            // .disable()
            .csrf(csrf -> csrf.disable())
                // .authorizeHttpRequests()
                // .requestMatchers("/api/v1/auth/**")
                // .permitAll()
                // .anyRequest()
                // .authenticated()
                .authorizeHttpRequests(req -> req.requestMatchers("api/v1/auth/**").permitAll().anyRequest().authenticated())
                // .sessionManagement()
                // .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(JwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
