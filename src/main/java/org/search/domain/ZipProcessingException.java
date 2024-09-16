package org.search.domain;

public class ZipProcessingException extends MovieSearchException {
    public ZipProcessingException(String message) {
        super(message);
    }

    public ZipProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}