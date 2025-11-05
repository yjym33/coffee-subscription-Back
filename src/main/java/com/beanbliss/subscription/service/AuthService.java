package com.beanbliss.subscription.service;

import com.beanbliss.subscription.dto.AuthDtos;
import com.beanbliss.subscription.entity.User;
import com.beanbliss.subscription.repository.UserRepository;
import com.beanbliss.subscription.security.JwtTokenProvider;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    @Timed(value = "auth.signup", description = "Time taken for user signup")
    public void signup(AuthDtos.SignupRequest request) {
        log.info("Attempting to signup user with email: {}", request.getEmail());
        
        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Signup failed: Email {} already exists", request.getEmail());
            throw new IllegalArgumentException("Email already in use");
        }
        
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());
        user.setAddress(request.getAddress());
        user.setAdmin(false);
        userRepository.save(user);
        
        log.info("User successfully signed up with email: {}", request.getEmail());
    }

    @Transactional(readOnly = true)
    @Timed(value = "auth.signin", description = "Time taken for user signin")
    public AuthDtos.AuthResponse signin(AuthDtos.SigninRequest request) {
        log.info("Attempting to signin user with email: {}", request.getEmail());
        
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.warn("Signin failed: Email {} not found", request.getEmail());
                    return new IllegalArgumentException("Invalid credentials");
                });
        
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            log.warn("Signin failed: Invalid password for email {}", request.getEmail());
            throw new IllegalArgumentException("Invalid credentials");
        }
        
        String token = jwtTokenProvider.createToken(user.getEmail());
        AuthDtos.AuthResponse response = new AuthDtos.AuthResponse();
        response.setToken(token);
        response.setId(String.valueOf(user.getId()));
        response.setEmail(user.getEmail());
        response.setName(user.getName());
        
        log.info("User successfully signed in with email: {}", request.getEmail());
        return response;
    }
}


