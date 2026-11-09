package org.com.NotificationService.exception;

public class EmailIdentityAlreadyExistsException extends ApiException {
    public EmailIdentityAlreadyExistsException(String message) {
        super(ErrorCode.EMAIL_IDENTITY_ALREADY_EXISTS, message);
    }
}
