package com.lipari.bank.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> handleValidation(
      MethodArgumentNotValidException exception
  ) {
    var errors = exception.getBindingResult()
        .getFieldErrors()
        .stream()
        .collect(Collectors.toMap(
            error -> error.getField(),
            error -> error.getDefaultMessage()
        ));

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(Map.of(
            "error", "Validation failed",
            "fields", errors
        ));
  }
}