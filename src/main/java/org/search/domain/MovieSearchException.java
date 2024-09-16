package org.search.domain;

public class MovieSearchException extends RuntimeException {
    public MovieSearchException(String message) {
        super(message);
    }

    public MovieSearchException(String message, Throwable cause) {
        super(message, cause);
    }
}