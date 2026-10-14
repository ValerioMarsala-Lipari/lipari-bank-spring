package com.lipari.bank.shared.exception;

import java.net.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(AccountNotFoundException.class)
    public ProblemDetail handleAccountNotFound(AccountNotFoundException exception) {
        log.warn("Account not found");
        return problem(HttpStatus.NOT_FOUND, "Account not found", "The requested account was not found.");
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ProblemDetail handleCustomerNotFound(CustomerNotFoundException exception) {
        log.warn("Customer not found");
        return problem(HttpStatus.NOT_FOUND, "Customer not found", "The requested customer was not found.");
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ProblemDetail handleInsufficientFunds(InsufficientFundsException exception) {
        log.warn("Transfer rejected: insufficient funds");
        return problem(
                HttpStatus.UNPROCESSABLE_ENTITY,
                "Insufficient funds",
                "The account has insufficient funds to complete the transfer.");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException exception) {
        var errors = exception.getBindingResult().getFieldErrors().stream()
                .collect(java.util.stream.Collectors.toMap(
                        error -> error.getField(),
                        error -> error.getDefaultMessage() != null ? error.getDefaultMessage() : "Validation error",
                        (first, second) -> first));

        ProblemDetail problem =
                problem(HttpStatus.BAD_REQUEST, "Validation failed", "One or more fields contain invalid values.");
        problem.setProperty("errors", errors);
        log.warn("Request validation failed: {} field error(s)", errors.size());
        return problem;
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ProblemDetail handleDuplicateResource(DuplicateResourceException exception) {
        log.warn("Request rejected: duplicate resource");
        return problem(
                HttpStatus.CONFLICT, "Duplicate resource", "A resource with the supplied information already exists.");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrityViolation(DataIntegrityViolationException exception) {
        log.warn("Request rejected by a data integrity constraint");
        return problem(HttpStatus.CONFLICT, "Conflict", "The request conflicts with existing data.");
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ProblemDetail handleInvalidRefreshToken(InvalidRefreshTokenException exception) {
        log.warn("Refresh token rejected");
        return problem(HttpStatus.UNAUTHORIZED, "Unauthorized", "The refresh token is invalid or expired.");
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnexpectedException(Exception exception) {
        log.error("Unexpected error while processing request", exception);
        return problem(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal server error",
                "An unexpected error occurred. Please try again later.");
    }

    private ProblemDetail problem(HttpStatus status, String title, String detail) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);
        problem.setTitle(title);
        problem.setType(URI.create("about:blank"));
        return problem;
    }
}
