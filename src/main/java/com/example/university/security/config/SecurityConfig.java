package com.example.university.security.config;

import com.example.university.security.constants.EndpointConstants;
import com.example.university.security.token.TokenAuthenticationFilter;
import com.example.university.security.token.TokenProvider;
import com.example.university.user.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    private final TokenProvider tokenProvider;
    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public AuthenticationManager authManager(HttpSecurity http,
                                             PasswordEncoder passwordEncoder,
                                             UserDetailsService userDetailService) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailService)
                .passwordEncoder(passwordEncoder)
                .and()
                .build();
    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(tokenProvider, customUserDetailsService);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        createAuthorizationRules(http);
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> {
                    request.requestMatchers("/auth/**").permitAll().anyRequest().authenticated();
                });

        // Our custom Token based authentication filter
        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    private void createAuthorizationRules(HttpSecurity http) throws Exception {
        actuatorAuthorizationRule(http);
        publicAuthorizationRule(http);
    }

    private void actuatorAuthorizationRule(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.GET, EndpointConstants.HEALTH_ENDPOINT).permitAll());
    }

    private void publicAuthorizationRule(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.OPTIONS, EndpointConstants.ALL_PATHS).permitAll()
                .requestMatchers(HttpMethod.POST, EndpointConstants.AUTHENTICATE).permitAll()
                .requestMatchers(HttpMethod.GET, EndpointConstants.API_UNIVERSITY).permitAll()
                .requestMatchers(HttpMethod.POST, EndpointConstants.API_UNIVERSITY).permitAll()
                .requestMatchers(HttpMethod.PUT, EndpointConstants.API_UNIVERSITY).permitAll()
                .requestMatchers(HttpMethod.DELETE, EndpointConstants.API_UNIVERSITY).permitAll()
                .requestMatchers(HttpMethod.DELETE, EndpointConstants.MAJOR).permitAll()
                .requestMatchers(HttpMethod.GET, EndpointConstants.CHECK_USER).permitAll()
                .requestMatchers(HttpMethod.POST, EndpointConstants.DEPARTMENT).permitAll()
                .requestMatchers(HttpMethod.DELETE, EndpointConstants.DEPARTMENT).permitAll()
                .requestMatchers(HttpMethod.PUT, EndpointConstants.DEPARTMENT).permitAll()
                .requestMatchers(HttpMethod.GET, EndpointConstants.DEPARTMENT).permitAll()
                .requestMatchers(HttpMethod.GET, EndpointConstants.USER).permitAll()
                .requestMatchers(HttpMethod.DELETE, EndpointConstants.USER).permitAll()
                .requestMatchers(HttpMethod.POST, EndpointConstants.USER).permitAll()
                .requestMatchers(HttpMethod.PUT, EndpointConstants.USER).permitAll()
                .requestMatchers(HttpMethod.PATCH, EndpointConstants.USER).permitAll()
                .requestMatchers(HttpMethod.GET, EndpointConstants.API_CLASSIFICATION).permitAll());
    }

}
