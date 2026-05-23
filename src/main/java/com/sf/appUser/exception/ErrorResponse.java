package com.sf.appUser.exception;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorResponse {

    private LocalDateTime timeStamp;
    private String status;
    private String error;
    private String message;
    private String path;
}
