package org.assesment.services.exceptions;

public class PdfNotFoundException extends ResourceNotFoundException {
    public PdfNotFoundException(String message) {
        super(message);
    }
}
