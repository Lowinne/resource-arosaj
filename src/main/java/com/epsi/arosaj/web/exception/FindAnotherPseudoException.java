package com.epsi.arosaj.web.exception;

public class FindAnotherPseudoException extends RuntimeException {
    public FindAnotherPseudoException() {
        super();
    }
    public FindAnotherPseudoException(final String message, final Throwable cause) {
        super(message, cause);
    }
    public FindAnotherPseudoException(final String message) {
        super(message);
    }
    public FindAnotherPseudoException(final Throwable cause) {
        super(cause);
    }
}
