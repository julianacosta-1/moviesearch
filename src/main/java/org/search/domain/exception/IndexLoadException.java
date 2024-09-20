package org.search.domain.exception;

public class IndexLoadException extends RuntimeException {
    public IndexLoadException(String message) {
        super(message);
    }

    public IndexLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
