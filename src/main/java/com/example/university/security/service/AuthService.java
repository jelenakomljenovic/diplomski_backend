package com.example.university.security.service;

import com.example.university.role.model.request.LoginRequest;
import com.example.university.security.model.JwtUserDetails;
import com.example.university.security.token.TokenProvider;
import com.example.university.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;

    public String login(LoginRequest request) {
        String token;
        try {

            Authentication authenticate = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            token = tokenProvider.createToken(authenticate);

        } catch (Exception ex) {
            log.error("MESSAGE: " + ex.getMessage());
            throw new BadCredentialsException("Wrong username or password...");
        }
        return token;
    }


    public JwtUserDetails getJwtUserDetailsFromRequest() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (JwtUserDetails) authentication.getPrincipal();
    }

    public Long getUserIdFromRequest() {
        JwtUserDetails jwtUserDetails = getJwtUserDetailsFromRequest();
        return jwtUserDetails.getId();
    }
}
