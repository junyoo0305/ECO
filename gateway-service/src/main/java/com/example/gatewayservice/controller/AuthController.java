package com.example.gatewayservice.controller;

import com.example.gatewayservice.dto.ApiResponse;
import com.example.gatewayservice.dto.LoginRequest;
import com.example.gatewayservice.dto.LoginResponse;
import com.example.gatewayservice.dto.RegisterRequest;
import com.example.gatewayservice.exception.InvalidPasswordException;
import com.example.gatewayservice.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<ApiResponse<LoginResponse>>> login(
            @RequestBody LoginRequest request) {

        return authService.login(request)
                .map(result ->
                        ResponseEntity.ok(
                                new ApiResponse<>(true, result, null)
                        )
                )
                // 승인 대기 → 403
                .onErrorResume(IllegalStateException.class, e ->
                        Mono.just(
                                ResponseEntity
                                        .status(HttpStatus.FORBIDDEN)
                                        .body(
                                                new ApiResponse<>(false, null, e.getMessage())
                                        )
                        )
                )
                // 비밀번호 틀림 → 401
                .onErrorResume(InvalidPasswordException.class, e ->
                        Mono.just(
                                ResponseEntity
                                        .status(HttpStatus.UNAUTHORIZED)
                                        .body(
                                                new ApiResponse<>(false, null, e.getMessage())
                                        )
                        )
                );
    }

    @GetMapping("/check-id")
    public Mono<ResponseEntity<Boolean>> checkId(
            @RequestParam String sellComId) {

        boolean exists = authService.existsSellComId(sellComId);
        return Mono.just(ResponseEntity.ok(exists));
    }

    @PostMapping("/register")
    public Mono<ResponseEntity<Void>> register(@RequestBody RegisterRequest request) {
        return authService.register(request)
                .thenReturn(ResponseEntity.ok().build());

    }

}
