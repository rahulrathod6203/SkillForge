package com.sf.appUser.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalException(Exception exception, WebRequest request) {
        log.error("An unexpected error occurred: ", exception);
        return buildResponse(request, "An internal server error occurred. Please try again later.", HttpStatus.INTERNAL_SERVER_ERROR, null);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException exception, WebRequest request) {
        log.warn("User not found exception: {}", exception.getMessage());
        return buildResponse(request, exception.getMessage(), HttpStatus.NOT_FOUND, null);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Object> handleEmailAlreadyExistsException(EmailAlreadyExistsException exception, WebRequest request) {
        log.warn("Email conflict exception: {}", exception.getMessage());
        return buildResponse(request, exception.getMessage(), HttpStatus.BAD_REQUEST, null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String defaultMessage = error.getDefaultMessage();
            errors.put(fieldName, defaultMessage);
        });
        log.warn("Validation failed for request path {}: {}", request.getDescription(false), errors);
        return buildResponse(request, "Validation failed!", HttpStatus.BAD_REQUEST, errors);
    }

    public ResponseEntity<Object> buildResponse(WebRequest request, String message, HttpStatus status, Map<String, String> errors) {

        String trimmedPath = request.getDescription(false).replace("uri=", "");

        ErrorDetails.ErrorDetailsBuilder builder = ErrorDetails.builder();

        if (errors != null && !errors.isEmpty()) {
            builder.fieldErrors(errors);
        }

        builder.timeStamp(Instant.now())
                .statusCode(String.valueOf(status.value()))
                .error(status.getReasonPhrase())
                .message(message)
                .path(trimmedPath);

        return ResponseEntity.status(status).body(builder.build());

    }

}
