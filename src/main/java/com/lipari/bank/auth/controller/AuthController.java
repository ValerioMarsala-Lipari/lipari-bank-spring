package com.lipari.bank.auth.controller;

import com.lipari.bank.auth.dto.RegisterRequest;
import com.lipari.bank.auth.dto.RegisterResponse;
import com.lipari.bank.auth.entity.AppUser;
import com.lipari.bank.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        AppUser appUser = authService.register(request);
        RegisterResponse response =
                new RegisterResponse(appUser.getId(), appUser.getUsername(), appUser.getEmail(), appUser.getRole());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
