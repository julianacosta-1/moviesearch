package org.search.domain.exception;

public class ZipProcessingException extends MovieSearchException {

    public ZipProcessingException(String message) {
        super(message);
    }

    public ZipProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}