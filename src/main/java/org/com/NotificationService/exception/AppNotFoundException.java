package org.com.NotificationService.exception;

public class AppNotFoundException extends ApiException{
    public AppNotFoundException(String message) {
        super(ErrorCode.APP_NOT_FOUND,message);
    }
}
