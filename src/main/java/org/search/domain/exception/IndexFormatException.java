package org.search.domain.exception;

public class IndexFormatException extends RuntimeException {
    public IndexFormatException(String message, Exception e) {
        super(message);
    }
}