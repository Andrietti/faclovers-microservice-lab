package com.gabriel.faclovers.production_service.shared;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException exception, HttpServletRequest request) {
        List<String> details = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();

        return build(HttpStatus.BAD_REQUEST, "Erro de validação", request.getRequestURI(), details);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiError> handleBusiness(BusinessException exception, HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST, exception.getMessage(), request.getRequestURI(), List.of());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(NotFoundException exception, HttpServletRequest request) {
        return build(HttpStatus.NOT_FOUND, exception.getMessage(), request.getRequestURI(), List.of());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleInternal(Exception exception, HttpServletRequest request) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno no servidor", request.getRequestURI(), List.of());
    }

    private ResponseEntity<ApiError> build(HttpStatus status, String message, String path, List<String> details) {
        ApiError apiError = new ApiError(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                path,
                details
        );

        return ResponseEntity.status(status).body(apiError);
    }
}
