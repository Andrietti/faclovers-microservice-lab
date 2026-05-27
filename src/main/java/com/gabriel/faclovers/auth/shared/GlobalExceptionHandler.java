package com.gabriel.faclovers.auth.shared;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiError> handleBusinessException(BusinessException exception, HttpServletRequest request) {
        var status = exception.getStatus();
        return ResponseEntity.status(status).body(buildError(status, exception.getMessage(), request.getRequestURI(), List.of()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationException(MethodArgumentNotValidException exception, HttpServletRequest request) {
        var details = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();

        var status = HttpStatus.BAD_REQUEST;
        return ResponseEntity.badRequest().body(buildError(status, "Erro de validação", request.getRequestURI(), details));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrityViolationException(DataIntegrityViolationException exception, HttpServletRequest request) {
        var status = HttpStatus.CONFLICT;
        return ResponseEntity.status(status).body(buildError(status, "Email já cadastrado", request.getRequestURI(), List.of()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleException(Exception exception, HttpServletRequest request) {
        var status = HttpStatus.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(status).body(buildError(status, "Erro interno do servidor", request.getRequestURI(), List.of()));
    }

    private ApiError buildError(HttpStatus status, String message, String path, List<String> details) {
        return new ApiError(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                path,
                details
        );
    }
}
