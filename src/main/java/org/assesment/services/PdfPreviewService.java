package org.assesment.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.assesment.config.SystemConfig;
import org.assesment.services.exceptions.PdfNotFoundException;

import java.awt.image.BufferedImage;
import java.io.*;

@ApplicationScoped
public class PdfPreviewService {

    @Inject
    private SystemConfig systemConfig;

    public byte[] generatePreviewImage(String fileName)  {
        File file = findFile(fileName);
        try {
            byte[] pdfBytes = this.convertFileToBytes(file);
            try (PDDocument document = PDDocument.load(pdfBytes)) {
                if (document.getNumberOfPages() > 0) {
                    PDFRenderer renderer = new PDFRenderer(document);
                    BufferedImage image = renderer.renderImageWithDPI(0, 300, ImageType.RGB);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    javax.imageio.ImageIO.write(image, "png", baos);
                    return baos.toByteArray();
                } else {
                    throw new IllegalArgumentException("Document is Empty.");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private File findFile(String fileName) {
        File dir = new File(systemConfig.getDirectory());
        String completeFileName = dir.getAbsolutePath() + File.separator + fileName;
        if (!completeFileName.contains(".pdf")){
            completeFileName = completeFileName+".pdf";
        }
        File file = new File(completeFileName);
        if (!file.exists()) {
            throw new PdfNotFoundException("The file "+ fileName +" could not be found");
        }
        return file;
    }

    public byte[] convertFileToBytes(File file) {
        try {
            try (FileInputStream fis = new FileInputStream(file);
                 BufferedInputStream bis = new BufferedInputStream(fis)) {
                byte[] fileBytes = new byte[(int) file.length()];
                bis.read(fileBytes);
                return fileBytes;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
