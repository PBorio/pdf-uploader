package org.assesment.controllers;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.assesment.services.SearchTextInPdfService;
import java.util.List;

@Path("/api/search")
public class PdfSearchController {

    @Inject
    private SearchTextInPdfService searchTextInPdfService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadPdf(@QueryParam("text") String text) {
        List<String> pdfWithTheGivenText = searchTextInPdfService.search(text);

        if (pdfWithTheGivenText.isEmpty()) {
            return Response.ok("No documents found for the given text").build();
        }

        return Response.ok(pdfWithTheGivenText).build();
    }

}
