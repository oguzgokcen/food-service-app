package com.apigateway.error;


import com.apigateway.exception.AuthException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GatewayExceptionHandler {
    @ExceptionHandler(AuthException.class)
    ResponseEntity<ApiError> handleAuthExceptions(AuthException exception) throws JsonProcessingException {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ApiError apiError1 = new ObjectMapper().readValue(exception.getMessage(), ApiError.class);
        return ResponseEntity.status(status).body(apiError1);
    }
}
