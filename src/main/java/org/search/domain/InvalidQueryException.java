package org.search.domain;

public class InvalidQueryException extends MovieSearchException {
    public InvalidQueryException(String message) {
        super(message);
    }
}