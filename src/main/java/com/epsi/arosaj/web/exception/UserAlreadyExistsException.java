package com.epsi.arosaj.web.exception;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException() {
        super();
    }
    public UserAlreadyExistsException(final String message, final Throwable cause) {
        super(message, cause);
    }
    public UserAlreadyExistsException(final String message) {
        super(message);
    }
    public UserAlreadyExistsException(final Throwable cause) {
        super(cause);
    }
}
