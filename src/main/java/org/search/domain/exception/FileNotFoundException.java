package org.search.domain.exception;

public class FileNotFoundException extends MovieSearchException {
    public FileNotFoundException(String message) {
        super(message);
    }

    public FileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}