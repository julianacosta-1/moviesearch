package org.search.domain.exception;

public class IndexSaveException extends RuntimeException {
    public IndexSaveException(String message) {
        super(message);
    }

    public IndexSaveException(String message, Throwable cause) {
        super(message, cause);
    }
}