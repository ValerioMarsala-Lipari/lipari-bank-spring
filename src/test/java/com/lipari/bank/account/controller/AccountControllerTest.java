package com.lipari.bank.account.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.lipari.bank.account.dto.AccountCreateRequest;
import com.lipari.bank.account.dto.AccountResponse;
import com.lipari.bank.account.entity.AccountStatus;
import com.lipari.bank.account.service.AccountService;
import com.lipari.bank.auth.filter.JwtAuthenticationFilter;
import com.lipari.bank.auth.service.CustomUserDetailsService;
import com.lipari.bank.auth.service.JwtService;
import com.lipari.bank.shared.config.SecurityConfig;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.lipari.bank.shared.exception.AccountNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.boot.security.autoconfigure.web.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(AccountController.class)
@ImportAutoConfiguration({SecurityAutoConfiguration.class, SecurityFilterAutoConfiguration.class})
@Import({SecurityConfig.class, JwtAuthenticationFilter.class})
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AccountService accountService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    private AccountResponse createAccountResponse() {
        return AccountResponse.builder()
                .id(42L)
                .iban("IT60X0542811101000000123456")
                .fiscalCode("RSSMRA80A01H501Z")
                .balance(new BigDecimal("1000.00"))
                .status(AccountStatus.ACTIVE)
                .createdAt(LocalDateTime.of(2026, 10, 2, 10, 30))
                .build();
    }

    @Test
    void findById_whenAccountExists_returnsAccount() throws Exception {
        // Given
        AccountResponse response = createAccountResponse();
        when(accountService.findById(42L)).thenReturn(response);

        // When / Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/accounts/42")
                        .with(user("test-customer").roles("CUSTOMER")))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(42))
                .andExpect(jsonPath("$.iban").value("IT60X0542811101000000123456"))
                .andExpect(jsonPath("$.fiscalCode").value("RSSMRA80A01H501Z"))
                .andExpect(jsonPath("$.balance").value(1000.00))
                .andExpect(jsonPath("$.status").value("ACTIVE"));

        verify(accountService).findById(42L);
    }

    @Test
    void findById_whenUnauthenticated_returns401() throws Exception {
        // When / Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/accounts/42")).andExpect(status().isUnauthorized());
    }

    @Test
    void create_whenRequestIsValid_returns201() throws Exception {
        // Given
        AccountResponse response = createAccountResponse();
        when(accountService.create(any(AccountCreateRequest.class))).thenReturn(response);

        String requestBody = """
                {
                    "fiscalCode": "RSSMRA80A01H501Z",
                    "iban": "IT60X0542811101000000123456",
                    "initialBalance": 1000.00
                }
                """;

        // When / Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/accounts")
                        .with(user("test-customer").roles("CUSTOMER"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.endsWith("/api/v1/accounts/42")))
                .andExpect(jsonPath("$.id").value(42))
                .andExpect(jsonPath("$.iban").value("IT60X0542811101000000123456"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));

        verify(accountService).create(any(AccountCreateRequest.class));
    }

    @Test
    void findById_whenAccountDoesNotExist_returns404() throws Exception {
        when(accountService.findById(99999L))
            .thenThrow(new AccountNotFoundException("99999"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/accounts/99999")
                .with(user("test-customer").roles("CUSTOMER")))
            .andExpect(status().isNotFound())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
            .andExpect(jsonPath("$.title").value("Account not found"))
            .andExpect(jsonPath("$.detail").value("The requested account was not found."));

        verify(accountService).findById(99999L);
    }
}
