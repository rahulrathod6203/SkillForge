package com.sf.auth.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalException(Exception exception, WebRequest request) {
        log.error("CRITICAL ERROR: An unexpected system failure occurred at path {}: ",
                request.getDescription(false).replace("uri=", ""), exception);
        return buildResponse(request, "An internal server error occurred. Please try again later.", HttpStatus.INTERNAL_SERVER_ERROR, null);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleUBadCredentialsException(BadCredentialsException exception, WebRequest request) {
        log.warn("Authentication rejected: Bad credentials provided.");
        return buildResponse(request, "Invalid email or password structure.", HttpStatus.UNAUTHORIZED, null);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException exception, WebRequest request) {
        log.warn("Resource access warning: User lookup failed at path {}. Error: {}",
                request.getDescription(false).replace("uri=", ""), exception.getMessage());

        return buildResponse(request, exception.getMessage(), HttpStatus.NOT_FOUND, null);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Object> handleEmailAlreadyExistsException(EmailAlreadyExistsException exception, WebRequest request) {
        log.warn("Data conflict encountered: Email already registered at path {}. Details: {}",
                request.getDescription(false).replace("uri=", ""), exception.getMessage());

        return buildResponse(request, exception.getMessage(), HttpStatus.BAD_REQUEST, null);
    }

    @ExceptionHandler(PhoneAlreadyExistsException.class)
    public ResponseEntity<Object> handlePhoneAlreadyExistsException(PhoneAlreadyExistsException exception, WebRequest request) {
        log.warn("Data conflict encountered: Phone number already registered at path {}. Details: {}",
                request.getDescription(false).replace("uri=", ""), exception.getMessage());

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

        // 🟡 Form/DTO request constraint violations logged cleanly alongside mapped field errors
        log.warn("Validation constraint failure for request path {}: Field ErrorsMap -> {}",
                request.getDescription(false).replace("uri=", ""), errors);

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