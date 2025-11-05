package com.beanbliss.subscription.service;

import com.beanbliss.subscription.dto.AuthDtos;
import com.beanbliss.subscription.entity.User;
import com.beanbliss.subscription.repository.UserRepository;
import com.beanbliss.subscription.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Testcontainers
@Transactional
@DisplayName("AuthService 테스트")
class AuthServiceTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.flyway.enabled", () -> "false");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 성공")
    void signup_Success() {
        // given
        AuthDtos.SignupRequest request = new AuthDtos.SignupRequest();
        request.setEmail("test@example.com");
        request.setPassword("password123");
        request.setName("Test User");
        request.setAddress("Test Address");

        // when
        authService.signup(request);

        // then
        User savedUser = userRepository.findByEmail("test@example.com")
                .orElseThrow();
        assertThat(savedUser.getEmail()).isEqualTo("test@example.com");
        assertThat(savedUser.getName()).isEqualTo("Test User");
        assertThat(savedUser.getAddress()).isEqualTo("Test Address");
        assertThat(passwordEncoder.matches("password123", savedUser.getPasswordHash())).isTrue();
        assertThat(savedUser.isAdmin()).isFalse();
    }

    @Test
    @DisplayName("중복 이메일로 회원가입 실패")
    void signup_DuplicateEmail_ThrowsException() {
        // given
        User existingUser = new User();
        existingUser.setEmail("existing@example.com");
        existingUser.setPasswordHash(passwordEncoder.encode("password123"));
        existingUser.setName("Existing User");
        userRepository.save(existingUser);

        AuthDtos.SignupRequest request = new AuthDtos.SignupRequest();
        request.setEmail("existing@example.com");
        request.setPassword("password123");
        request.setName("New User");

        // when & then
        assertThatThrownBy(() -> authService.signup(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Email already in use");
    }

    @Test
    @DisplayName("로그인 성공")
    void signin_Success() {
        // given
        User user = new User();
        user.setEmail("test@example.com");
        user.setPasswordHash(passwordEncoder.encode("password123"));
        user.setName("Test User");
        userRepository.save(user);

        AuthDtos.SigninRequest request = new AuthDtos.SigninRequest();
        request.setEmail("test@example.com");
        request.setPassword("password123");

        // when
        AuthDtos.AuthResponse response = authService.signin(request);

        // then
        assertThat(response.getToken()).isNotBlank();
        assertThat(response.getEmail()).isEqualTo("test@example.com");
        assertThat(response.getName()).isEqualTo("Test User");
        assertThat(response.getId()).isNotNull();
    }

    @Test
    @DisplayName("존재하지 않는 이메일로 로그인 실패")
    void signin_NonExistentEmail_ThrowsException() {
        // given
        AuthDtos.SigninRequest request = new AuthDtos.SigninRequest();
        request.setEmail("nonexistent@example.com");
        request.setPassword("password123");

        // when & then
        assertThatThrownBy(() -> authService.signin(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid credentials");
    }

    @Test
    @DisplayName("잘못된 비밀번호로 로그인 실패")
    void signin_WrongPassword_ThrowsException() {
        // given
        User user = new User();
        user.setEmail("test@example.com");
        user.setPasswordHash(passwordEncoder.encode("correctpassword"));
        user.setName("Test User");
        userRepository.save(user);

        AuthDtos.SigninRequest request = new AuthDtos.SigninRequest();
        request.setEmail("test@example.com");
        request.setPassword("wrongpassword");

        // when & then
        assertThatThrownBy(() -> authService.signin(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid credentials");
    }
}

