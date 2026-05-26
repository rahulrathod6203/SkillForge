package com.awp.auth.exception.responseBuilder;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;
import java.util.Map;

@Component
public class ExceptionResponseBuilder {

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
