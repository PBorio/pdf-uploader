package org.assesment.services.exceptions;

import jakarta.ws.rs.WebApplicationException;

public class MoreThanOneFileUploadedException extends WebApplicationException {
    public MoreThanOneFileUploadedException(String message) {
        super(message);
    }
}
