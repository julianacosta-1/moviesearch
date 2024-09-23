package org.search.domain.exception;

public class ZipFileEmptyException extends RuntimeException {
    public ZipFileEmptyException(String message) {
        super(message);
    }
}