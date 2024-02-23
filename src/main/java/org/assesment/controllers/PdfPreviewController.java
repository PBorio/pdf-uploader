package org.assesment.controllers;


import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.assesment.services.PdfPreviewService;

@Path("/api/preview")
public class PdfPreviewController {

    @Inject
    private PdfPreviewService pdfPreviewService;

    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Path("/{file}")
    public Response uploadPdf(@PathParam("file") String file) {
        byte[] previewImageBytes = pdfPreviewService.generatePreviewImage(file);
        return Response.ok(previewImageBytes, "image/jpeg")
                  .header("Content-Disposition", "inline; filename=preview.jpg")
                  .build();
    }


}
