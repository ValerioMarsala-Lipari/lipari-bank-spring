package com.lipari.bank.auth.service;

import com.lipari.bank.auth.dto.*;
import com.lipari.bank.auth.entity.AppUser;
import com.lipari.bank.auth.entity.AppUserRole;
import com.lipari.bank.auth.repository.AppUserRepository;
import com.lipari.bank.shared.exception.InvalidRefreshTokenException;
import com.lipari.bank.shared.exception.UsernameAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final CustomUserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AppUser register(RegisterRequest request) {
        log.info("Registering new user");
        if (appUserRepository.existsByUsername(request.username())) {
            log.warn("Registration rejected: username already exists");
            throw new UsernameAlreadyExistsException(request.username());
        }

        AppUser appUser = new AppUser();
        appUser.setUsername(request.username());
        appUser.setPassword(passwordEncoder.encode(request.password()));
        appUser.setEmail(request.email());
        appUser.setRole(AppUserRole.USER);

        AppUser saved = appUserRepository.save(appUser);
        log.info("User registered successfully with id: {}", saved.getId());
        return saved;
    }

    public LoginResponse login(LoginRequest request) {
        log.info("Processing login request");
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password()));

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.username());

        String accessToken = jwtService.generateAccessToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        log.info("User authenticated successfully");
        return new LoginResponse(accessToken, refreshToken);
    }

    public RefreshTokenResponse refresh(RefreshTokenRequest request) {
        log.info("Processing refresh token request");
        String refreshToken = request.refreshToken();

        if (!jwtService.isRefreshToken(refreshToken)) {
            log.warn("Refresh token rejected");
            throw new InvalidRefreshTokenException();
        }

        String username = jwtService.extractUsername(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (!jwtService.isTokenValid(refreshToken, userDetails)) {
            log.warn("Refresh token rejected");
            throw new InvalidRefreshTokenException();
        }

        String accessToken = jwtService.generateAccessToken(userDetails);

        log.info("Access token refreshed successfully");

        return new RefreshTokenResponse(accessToken);
    }
}
