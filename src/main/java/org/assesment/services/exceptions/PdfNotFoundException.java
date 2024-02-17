package org.assesment.services.exceptions;

public class PdfNotFoundException extends RuntimeException {
    public PdfNotFoundException(String message) {
        super(message);
    }
}
