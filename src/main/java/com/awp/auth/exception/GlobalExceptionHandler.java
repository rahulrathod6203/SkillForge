package com.awp.auth.exception;

import com.awp.auth.exception.responseBuilder.ExceptionResponseBuilder;
import com.awp.auth.exception.userDomain.EmailAlreadyExistsException;
import com.awp.auth.exception.userDomain.PhoneAlreadyExistsException;
import com.awp.auth.exception.userDomain.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ExceptionResponseBuilder responseBuilder;

    // ********************** Security Exceptions *****************************

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException exception, WebRequest request) {
        log.warn("Access denied...");
        return responseBuilder.buildResponse(request, "Access Denied: You do not have permission to view or modify this resource!", HttpStatus.FORBIDDEN, null);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException exception, WebRequest request) {
        log.warn("Authentication rejected: Bad credentials provided.");
        return responseBuilder.buildResponse(request, "Invalid email or password!.", HttpStatus.UNAUTHORIZED, null);
    }

// ********************** User Domain Exceptions *****************************

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException exception, WebRequest request) {
        log.warn("Resource access warning: User lookup failed at path {}. Error: {}",
                request.getDescription(false).replace("uri=", ""), exception.getMessage());
        return responseBuilder.buildResponse(request, exception.getMessage(), HttpStatus.NOT_FOUND, null);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Object> handleEmailAlreadyExistsException(EmailAlreadyExistsException exception, WebRequest request) {
        log.warn("Data conflict encountered: Email already registered at path {}. Details: {}",
                request.getDescription(false).replace("uri=", ""), exception.getMessage());
        return responseBuilder.buildResponse(request, exception.getMessage(), HttpStatus.BAD_REQUEST, null);
    }

    @ExceptionHandler(PhoneAlreadyExistsException.class)
    public ResponseEntity<Object> handlePhoneAlreadyExistsException(PhoneAlreadyExistsException exception, WebRequest request) {
        log.warn("Data conflict encountered: Phone number already registered at path {}. Details: {}",
                request.getDescription(false).replace("uri=", ""), exception.getMessage());
        return responseBuilder.buildResponse(request, exception.getMessage(), HttpStatus.BAD_REQUEST, null);
    }

    // ********************** Global Exceptions *****************************

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String defaultMessage = error.getDefaultMessage();
            errors.put(fieldName, defaultMessage);
        });

        log.warn("Validation constraint failure for request path {}: Field ErrorsMap -> {}", request.getDescription(false).replace("uri=", ""), errors);

        return responseBuilder.buildResponse(request, "Validation failed!", HttpStatus.BAD_REQUEST, errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalException(Exception exception, WebRequest request) {
        log.error("CRITICAL ERROR: An unexpected system failure occurred at path {}: ", request.getDescription(false).replace("uri=", ""), exception);
        return responseBuilder.buildResponse(request, "An internal server error occurred. Please try again later!.", HttpStatus.INTERNAL_SERVER_ERROR, null);
    }
}