package com.lipari.bank.branch.controller;

import com.lipari.bank.branch.dto.BranchCreateRequest;
import com.lipari.bank.branch.dto.BranchResponse;
import com.lipari.bank.branch.service.BranchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/branches")
@RequiredArgsConstructor
@Tag(name = "Branches", description = "Branch management API")
public class BranchController {

    private final BranchService branchService;

    @GetMapping
    @Operation(summary = "Get all branches", description = "Returns all branches")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Branches retrieved successfully")})
    public ResponseEntity<List<BranchResponse>> findAll() {
        return ResponseEntity.ok(branchService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get branch by ID", description = "Returns a branch by its ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Branch found"),
        @ApiResponse(responseCode = "404", description = "Branch not found")
    })
    public ResponseEntity<BranchResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(branchService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Create branch", description = "Creates a new branch")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Branch created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid branch data")
    })
    public ResponseEntity<BranchResponse> create(@Valid @RequestBody BranchCreateRequest request) {
        return ResponseEntity.status(201).body(branchService.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update branch", description = "Updates an existing branch")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Branch updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid branch data"),
        @ApiResponse(responseCode = "404", description = "Branch not found")
    })
    public ResponseEntity<BranchResponse> update(
            @PathVariable Long id, @Valid @RequestBody BranchCreateRequest request) {
        return ResponseEntity.ok(branchService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete branch", description = "Deletes a branch by its ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Branch deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Branch not found")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        branchService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
