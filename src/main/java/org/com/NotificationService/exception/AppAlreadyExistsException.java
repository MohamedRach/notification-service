package org.com.NotificationService.exception;

public class AppAlreadyExistsException extends ApiException{
    public AppAlreadyExistsException(String message) {
        super(ErrorCode.APP_ALREADY_EXISTS, message);
    }
}
