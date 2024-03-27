package com.epsi.arosaj.web.exception;

public class ParameterMistakeException extends RuntimeException {
    public ParameterMistakeException() {
        super();
    }
    public ParameterMistakeException(final String message, final Throwable cause) {
        super(message, cause);
    }
    public ParameterMistakeException(final String message) {
        super(message);
    }
    public ParameterMistakeException(final Throwable cause) {
        super(cause);
    }
}
