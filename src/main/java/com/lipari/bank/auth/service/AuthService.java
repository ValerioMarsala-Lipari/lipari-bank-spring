package com.lipari.bank.auth.service;

import com.lipari.bank.auth.dto.RegisterRequest;
import com.lipari.bank.auth.entity.AppUser;
import com.lipari.bank.auth.entity.AppUserRole;
import com.lipari.bank.auth.repository.AppUserRepository;
import com.lipari.bank.shared.exception.UsernameAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AppUser register(RegisterRequest request) {
        if (appUserRepository.existsByUsername(request.username())) {
            throw new UsernameAlreadyExistsException(request.username());
        }

        AppUser appUser = new AppUser();
        appUser.setUsername(request.username());
        appUser.setPassword(passwordEncoder.encode(request.password()));
        appUser.setEmail(request.email());
        appUser.setRole(AppUserRole.USER);

        return appUserRepository.save(appUser);
    }
}
