package org.com.NotificationService.exception;

public class EmailChanelCreationException extends ApiException {
    public EmailChanelCreationException(String message, Throwable cause) {
        super(ErrorCode.EMAIL_CHANEL_CREATION_FAILED, message, cause);
    }
}
