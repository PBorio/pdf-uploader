package org.assesment.controllers;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.assesment.services.PdfUploadService;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

@Path("/api/upload")
public class PdfUploaderController {

    @Inject
    PdfUploadService pdfUploadService;

    @POST
    @Path("/pdf")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_PLAIN)
    public Response fileUpload(@MultipartForm MultipartFormDataInput
                                       input) {
        try {
            String result = pdfUploadService.uploadFile(input);
            return Response.ok().
                    entity(result).build();
        } catch (RuntimeException e){
            String message = "Bad Request: "+ e.getMessage();
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(message)
                    .build();
        }
    }


}
