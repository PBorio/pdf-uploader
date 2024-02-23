package org.assesment.services.exceptions;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;


@Provider
public class GenericExceptionMappers implements ExceptionMapper<RuntimeException> {

    @Override
    public Response toResponse(RuntimeException ex) {
        ex.printStackTrace();

        if (ex instanceof ResourceNotFoundException) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Not Found:  " + ex.getMessage())
                    .build();
        } else if (ex instanceof WebApplicationException) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Bad Request: " + ex.getMessage())
                    .build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Internal Server Error: " + ex.getMessage())
                    .build();
        }
    }
}
