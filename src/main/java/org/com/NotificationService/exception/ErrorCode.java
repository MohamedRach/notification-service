package org.com.NotificationService.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    APP_ALREADY_EXISTS(HttpStatus.CONFLICT, "An application with the same name already exists."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred."),
    APP_NOT_FOUND(HttpStatus.NOT_FOUND, "The requested application was not found."),
    EMAIL_IDENTITY_ALREADY_EXISTS(HttpStatus.CONFLICT, "An email identity already exists for this domain."),
    EMAIL_CHANEL_CREATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create the email channel.");

    private final HttpStatus status;
    private final String title;

    ErrorCode(HttpStatus status, String title) {
        this.status = status;
        this.title = title;
    }
    public HttpStatus getStatus() { return status; }
    public String getTitle() { return title; }
}
