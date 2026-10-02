package com.lipari.bank.customer.controller;

import com.lipari.bank.customer.dto.CustomerCreateRequest;
import com.lipari.bank.customer.dto.CustomerResponse;
import com.lipari.bank.customer.dto.CustomerUpdateRequest;
import com.lipari.bank.customer.service.CustomerService;
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
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@Tag(
    name = "Customers",
    description = "Customer management API"
)
public class CustomerController {

  private final CustomerService customerService;

  @GetMapping
  @Operation(
      summary = "Get all customers",
      description = "Returns all customers"
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "200",
          description = "Customers retrieved successfully"
      )
  })
  public ResponseEntity<List<CustomerResponse>> findAll() {
    return ResponseEntity.ok(customerService.findAll());
  }

  @GetMapping("/{id}")
  @Operation(
      summary = "Get customer by ID",
      description = "Returns a single customer by its ID"
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "200",
          description = "Customer found"
      ),
      @ApiResponse(
          responseCode = "404",
          description = "Customer not found"
      )
  })
  public ResponseEntity<CustomerResponse> findById(
      @Parameter(description = "Customer ID", example = "1")
      @PathVariable
      @Positive
      Long id
  ) {
    return ResponseEntity.ok(customerService.findById(id));
  }

  @PostMapping
  @Operation(
      summary = "Create customer",
      description = "Creates a new customer"
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "201",
          description = "Customer created successfully"
      ),
      @ApiResponse(
          responseCode = "400",
          description = "Invalid customer data"
      )
  })
  public ResponseEntity<CustomerResponse> create(
      @Valid
      @RequestBody
      CustomerCreateRequest request
  ) {
    CustomerResponse response = customerService.create(request);

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
      summary = "Update customer",
      description = "Updates an existing customer"
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "200",
          description = "Customer updated successfully"
      ),
      @ApiResponse(
          responseCode = "400",
          description = "Invalid customer data"
      ),
      @ApiResponse(
          responseCode = "404",
          description = "Customer not found"
      )
  })
  public ResponseEntity<CustomerResponse> update(
      @Parameter(description = "Customer ID", example = "1")
      @PathVariable
      @Positive
      Long id,

      @Valid
      @RequestBody
      CustomerUpdateRequest request
  ) {
    return ResponseEntity.ok(
        customerService.update(id, request)
    );
  }

  @DeleteMapping("/{id}")
  @Operation(
      summary = "Delete customer",
      description = "Deletes an existing customer"
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "204",
          description = "Customer deleted successfully"
      ),
      @ApiResponse(
          responseCode = "404",
          description = "Customer not found"
      )
  })
  public ResponseEntity<Void> delete(
      @Parameter(description = "Customer ID", example = "1")
      @PathVariable
      @Positive
      Long id
  ) {
    customerService.delete(id);
    return ResponseEntity.noContent().build();
  }
}