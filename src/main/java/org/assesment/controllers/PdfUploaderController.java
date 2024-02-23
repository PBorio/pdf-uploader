package org.assesment.controllers;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import org.assesment.services.PdfUploadService;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import java.net.URI;

@Path("/api/upload")
public class PdfUploaderController {

    @Inject
    PdfUploadService pdfUploadService;


    @POST
    @Path("/pdf")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response fileUpload(@MultipartForm MultipartFormDataInput input) {
        String resourceId = pdfUploadService.uploadFile(input);
        URI resourceURI = UriBuilder.fromResource(PdfPreviewController.class)
                .path("/{id}")
                .build(resourceId);
        return Response.created(resourceURI).build();
    }




}
