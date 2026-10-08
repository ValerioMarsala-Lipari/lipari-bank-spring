package com.lipari.bank.auth.dto;

import com.lipari.bank.auth.entity.AppUserRole;

public record RegisterResponse(Long id, String username, String email, AppUserRole role) {}
