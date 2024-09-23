package org.search.domain.exception;

public class IndexEmptyException extends RuntimeException{
    public IndexEmptyException(String message) {
        super(message);
    }

    public IndexEmptyException(String message, Throwable cause) {
        super(message, cause);
    }
}