package org.assesment.services;

import jakarta.enterprise.context.ApplicationScoped;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class SearchTextInPdfService {

    @ConfigProperty(name = "upload.directory")
    String UPLOAD_DIR;
    public List<String> search(String text) {
        File pdfsDir = new File(UPLOAD_DIR);
        List<String> foundIn = new ArrayList<>();


        File[] pdfs = pdfsDir.listFiles((dir, nome) -> nome.toLowerCase().endsWith(".pdf"));

        if (pdfs != null) {
            for (File pdf : pdfs) {
                try {
                    if (textInPdf(pdf, text)) {
                        foundIn.add(pdf.getName());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }
        return foundIn;
    }

    private static boolean textInPdf(File pdf, String text) throws IOException {
        try (PDDocument document = PDDocument.load(pdf)) {
            PDFTextStripper pdfTextStripper = new PDFTextStripper();
            String pdfText = pdfTextStripper.getText(document);

            return pdfText.contains(text);
        }
    }

}
