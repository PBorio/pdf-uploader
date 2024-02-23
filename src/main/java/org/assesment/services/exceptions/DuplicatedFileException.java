package org.assesment.services.exceptions;

import jakarta.ws.rs.WebApplicationException;

public class DuplicatedFileException extends WebApplicationException {
    public DuplicatedFileException(String message) {
        super(message);
    }
}
