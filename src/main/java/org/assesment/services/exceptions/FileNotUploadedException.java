package org.assesment.services.exceptions;

import jakarta.ws.rs.WebApplicationException;

public class FileNotUploadedException extends WebApplicationException {
    public FileNotUploadedException(String message) {
        super(message);
    }
}
