package com.beanbliss.subscription.controller;

import com.beanbliss.subscription.dto.AuthDtos;
import com.beanbliss.subscription.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication", description = "인증 API")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "회원가입",
            description = "새로운 사용자를 등록합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "회원가입 성공",
                    content = @Content(schema = @Schema(implementation = Void.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 (이메일 중복 등)"
            )
    })
    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody AuthDtos.SignupRequest request) {
        authService.signup(request);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "로그인",
            description = "이메일과 비밀번호로 로그인하고 JWT 토큰을 받습니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "로그인 성공",
                    content = @Content(schema = @Schema(implementation = AuthDtos.AuthResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 실패 (잘못된 이메일 또는 비밀번호)"
            )
    })
    @PostMapping("/signin")
    public ResponseEntity<AuthDtos.AuthResponse> signin(@Valid @RequestBody AuthDtos.SigninRequest request) {
        return ResponseEntity.ok(authService.signin(request));
    }
}


