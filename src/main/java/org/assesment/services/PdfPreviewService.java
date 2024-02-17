package org.assesment.services;

import jakarta.enterprise.context.ApplicationScoped;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.assesment.services.exceptions.PdfNotFoundException;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.awt.image.BufferedImage;
import java.io.*;

@ApplicationScoped
public class PdfPreviewService {

    @ConfigProperty(name = "upload.directory")
    String UPLOAD_DIR;

    public byte[] generatePreviewImage(String fileName) throws IOException {

        File dir = new File(UPLOAD_DIR);
        String completeFileName = dir.getAbsolutePath() + File.separator + fileName+ ".pdf";

        byte[] pdfBytes = this.convertFileToBytes(completeFileName);

        try (PDDocument document = PDDocument.load(pdfBytes)) {

            if (document.getNumberOfPages() > 0) {
                PDFRenderer renderer = new PDFRenderer(document);
                BufferedImage image = renderer.renderImageWithDPI(0, 300, ImageType.RGB);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                javax.imageio.ImageIO.write(image, "png", baos);
                return baos.toByteArray();
            } else {
                throw new IllegalArgumentException("O documento PDF está vazio.");
            }
        }
    }

    public static byte[] convertFileToBytes(String filePath) throws IOException {
        File file = new File(filePath);

        if (!file.exists()) {
            throw new PdfNotFoundException("The file "+filePath+" could not be found");
        }

        try (FileInputStream fis = new FileInputStream(file);
             BufferedInputStream bis = new BufferedInputStream(fis)) {

            // Crie um array de bytes com o tamanho do arquivo
            byte[] fileBytes = new byte[(int) file.length()];

            // Leia os bytes do arquivo para o array
            bis.read(fileBytes);

            return fileBytes;
        }
    }
}
