package org.search.domain.exception;

public class SearchTermNotFoundException extends MovieSearchException {
    public SearchTermNotFoundException(String message) {
        super(message);
    }
}