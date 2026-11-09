package org.com.NotificationService.exception;

public class DomainNotFoundException extends ApiException {
    public DomainNotFoundException(String message) {
        super(ErrorCode.APP_NOT_FOUND, message);
    }
}
