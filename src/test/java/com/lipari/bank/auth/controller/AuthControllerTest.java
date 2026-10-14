package com.lipari.bank.auth.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.lipari.bank.auth.dto.RegisterRequest;
import com.lipari.bank.auth.filter.JwtAuthenticationFilter;
import com.lipari.bank.auth.service.AuthService;
import com.lipari.bank.auth.service.CustomUserDetailsService;
import com.lipari.bank.auth.service.JwtService;
import com.lipari.bank.shared.config.SecurityConfig;
import com.lipari.bank.shared.exception.AccountNotFoundException;
import com.lipari.bank.shared.exception.UsernameAlreadyExistsException;
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

@WebMvcTest(AuthController.class)
@ImportAutoConfiguration({
    SecurityAutoConfiguration.class,
    SecurityFilterAutoConfiguration.class
})
@Import({SecurityConfig.class, JwtAuthenticationFilter.class})
class AuthControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private AuthService authService;

  @MockitoBean
  private JwtService jwtService;

  @MockitoBean
  private CustomUserDetailsService customUserDetailsService;

  @Test
  void register_whenUsernameAlreadyExists_returns409() throws Exception {
    when(authService.register(any(RegisterRequest.class)))
        .thenThrow(new UsernameAlreadyExistsException("Username already exists"));

    String requestBody = """
                {
                    "username": "existing-user",
                    "password": "ValidPassword123!",
                    "email": "existing@example.com"
                }
                """;

    mockMvc.perform(post("/api/v1/auth/register")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.title").value("Duplicate resource"))
        .andExpect(jsonPath("$.detail")
            .value("A resource with the supplied information already exists."));

    verify(authService).register(any(RegisterRequest.class));
  }
}