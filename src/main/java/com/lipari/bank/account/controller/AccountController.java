package com.lipari.bank.account.controller;

import com.lipari.bank.account.dto.AccountCreateRequest;
import com.lipari.bank.account.dto.AccountResponse;
import com.lipari.bank.account.dto.AccountUpdateRequest;
import com.lipari.bank.account.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
@Tag(
    name = "Accounts",
    description = "Account management API"
)
public class AccountController {

  private final AccountService accountService;

  @GetMapping
  @Operation(
      summary = "Get all accounts",
      description = "Returns all accounts"
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "200",
          description = "Accounts retrieved successfully"
      )
  })
  public ResponseEntity<List<AccountResponse>> findAll() {
    return ResponseEntity.ok(accountService.findAll());
  }

  @GetMapping(value = "/search", params = "iban")
  @Operation(
      summary = "Search account by IBAN",
      description = "Returns an account matching the given IBAN"
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "200",
          description = "Account found"
      ),
      @ApiResponse(
          responseCode = "404",
          description = "Account not found"
      )
  })
  public ResponseEntity<AccountResponse> findByIban(
      @Parameter(description = "Account IBAN")
      @RequestParam
      String iban
  ) {
    return ResponseEntity.ok(accountService.findByIban(iban));
  }

  @GetMapping("/{id}")
  @Operation(
      summary = "Get account by ID",
      description = "Returns a single account by its ID"
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "200",
          description = "Account found"
      ),
      @ApiResponse(
          responseCode = "404",
          description = "Account not found"
      )
  })
  public ResponseEntity<AccountResponse> findById(
      @Parameter(description = "Account ID", example = "1")
      @PathVariable
      @Positive
      Long id
  ) {
    return ResponseEntity.ok(accountService.findById(id));
  }

  @PostMapping
  @Operation(
      summary = "Create account",
      description = "Creates a new account"
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "201",
          description = "Account created successfully"
      ),
      @ApiResponse(
          responseCode = "400",
          description = "Invalid account data"
      )
  })
  public ResponseEntity<AccountResponse> create(
      @Valid
      @RequestBody
      AccountCreateRequest request
  ) {
    AccountResponse response = accountService.create(request);

    URI location = ServletUriComponentsBuilder
        .fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(response.id())
        .toUri();

    return ResponseEntity
        .created(location)
        .body(response);
  }

  @PutMapping("/{id}")
  @Operation(
      summary = "Update account",
      description = "Updates an existing account"
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "200",
          description = "Account updated successfully"
      ),
      @ApiResponse(
          responseCode = "400",
          description = "Invalid account data"
      ),
      @ApiResponse(
          responseCode = "404",
          description = "Account not found"
      )
  })
  public ResponseEntity<AccountResponse> update(
      @Parameter(description = "Account ID", example = "1")
      @PathVariable
      @Positive
      Long id,

      @Valid
      @RequestBody
      AccountUpdateRequest request
  ) {
    return ResponseEntity.ok(
        accountService.update(id, request)
    );
  }

  @DeleteMapping("/{id}")
  @Operation(
      summary = "Delete account",
      description = "Deletes an existing account"
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "204",
          description = "Account deleted successfully"
      ),
      @ApiResponse(
          responseCode = "404",
          description = "Account not found"
      )
  })
  public ResponseEntity<Void> delete(
      @Parameter(description = "Account ID", example = "1")
      @PathVariable
      @Positive
      Long id
  ) {
    accountService.delete(id);
    return ResponseEntity.noContent().build();
  }
}