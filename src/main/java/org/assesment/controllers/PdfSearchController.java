package org.assesment.controllers;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.assesment.services.PdfPreviewService;
import org.assesment.services.SearchTextInPdfService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Path("/api/search")
public class PdfSearchController {

    @Inject
    private SearchTextInPdfService searchTextInPdfService;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/{text}")
    public Response uploadPdf(@PathParam("text") String text) {
        try {
            List<String> pdfWithTheGivenText = searchTextInPdfService.search(text);

            if (pdfWithTheGivenText.isEmpty()) {
                return Response.ok("No documents found for the given text").build();
            }
            StringBuilder result = new StringBuilder("The Text was found in the following documents: "+System.lineSeparator());
            pdfWithTheGivenText.forEach(e -> result.append("File: "+e+System.lineSeparator()));

            return Response.ok(result.toString()).build();
        } catch (RuntimeException e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).entity("Error searching files: "+e.getMessage()).build();
        }
    }

}
