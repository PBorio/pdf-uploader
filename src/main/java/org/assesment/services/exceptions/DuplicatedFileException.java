package org.assesment.services.exceptions;

public class DuplicatedFileException extends RuntimeException {
    public DuplicatedFileException(String message) {
        super(message);
    }
}
