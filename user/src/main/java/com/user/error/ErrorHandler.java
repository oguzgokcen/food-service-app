package com.user.error;

import com.user.exception.NotUniqueEmailException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

import static com.user.error.GlobalErrorCode.JWT_ERROR;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler({NotUniqueEmailException.class, MethodArgumentNotValidException.class, AuthenticationException.class})
    ResponseEntity<ApiError> handleException(Exception exception, HttpServletRequest request){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ApiError apiError = new ApiError();
        apiError.setPath(request.getRequestURI());
        apiError.setMessage(exception.getMessage());
        if(exception instanceof MethodArgumentNotValidException){
            var validationErrors = ((MethodArgumentNotValidException)exception).getBindingResult().getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage, (existing, replacing) -> existing));
            Map.Entry<String, String> firstEntry = validationErrors.entrySet().iterator().next();
            apiError.setMessage(firstEntry.getValue());
            apiError.setValidationErrors(validationErrors);
        }
        else if(exception instanceof AuthenticationException){
            apiError.setMessage("Şifre veya kullanıcı adı hatalı");
            status = HttpStatus.UNAUTHORIZED;
        }
        apiError.setStatus(status.value()+"");
        return ResponseEntity.status(status).body(apiError);
    }
    @ExceptionHandler({SignatureException.class, MalformedJwtException.class, ExpiredJwtException.class,UnsupportedJwtException.class})
    ResponseEntity<ApiError> handleJwtExceptions(Exception exception, HttpServletRequest request){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ApiError apiError = new ApiError();
        apiError.setPath(request.getRequestURI());
        apiError.setMessage("Ya token'ın süresi doldu, ya da hatalı token gönderdin amma hangisi oldu orasını ben bilemem kardeş :)");
        apiError.setStatus(JWT_ERROR);
        return ResponseEntity.status(status).body(apiError);
    }

}