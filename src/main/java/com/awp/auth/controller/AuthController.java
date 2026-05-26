package com.awp.auth.controller;

import com.awp.auth.dto.AuthResponse;
import com.awp.auth.dto.LoginDTO;
import com.awp.auth.dto.RegisterDTO;
import com.awp.auth.service.UserAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserAuthService userAuthService;

    @PostMapping(value = {"/login", "/sign-in"})
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginDTO loginDTO) {
        AuthResponse successResponse = userAuthService.login(loginDTO);
        return ResponseEntity.ok(successResponse);
    }

    @PostMapping(value = {"/register", "/sign-up"})
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterDTO registerDTO) {
        AuthResponse authResponse = userAuthService.register(registerDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/v1/users/{email}")
                .buildAndExpand(registerDTO.email())
                .toUri();
        return ResponseEntity.created(location).body(authResponse);
    }
}
