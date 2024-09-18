package org.search.domain.exception;

public class InvalidQueryException extends MovieSearchException {
    public InvalidQueryException(String message) {
        super(message);
    }
}