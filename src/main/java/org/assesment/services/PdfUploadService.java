package org.assesment.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MultivaluedMap;
import org.apache.commons.io.IOUtils;
import org.assesment.services.exceptions.DuplicatedFileException;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class PdfUploadService {
    @ConfigProperty(name = "upload.directory")
    String UPLOAD_DIR;

    @Inject
    private ExistingPdfValidatorService existingPdfValidatorService;

    public String uploadFile(MultipartFormDataInput input) {
        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        List<String> fileNames = new ArrayList<>();
        List<InputPart> inputParts = uploadForm.get("file");
        String fileName = null;
        for (InputPart inputPart : inputParts) {
            try {
                MultivaluedMap<String, String> header =
                        inputPart.getHeaders();
                fileName = getFileName(header);
                fileNames.add(fileName);
                InputStream inputStream = inputPart.getBody(InputStream.class, null);
                writeFile(inputStream,fileName);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        return "File Successfully Uploaded";
    }

    private void writeFile(InputStream inputStream,String fileName)
            throws IOException {
        byte[] bytes = IOUtils.toByteArray(inputStream);
        File customDir = new File(UPLOAD_DIR);
        fileName = customDir.getAbsolutePath() +
                File.separator + fileName;
        if (existingPdfValidatorService.isPdfAlreadyInDir(fileName)){
            new DuplicatedFileException("The file "+fileName+" is duplicated ");
        }
        Files.write(Paths.get(fileName), bytes,
                StandardOpenOption.CREATE_NEW);
    }

    private String getFileName(MultivaluedMap<String, String> header) {
        String[] contentDisposition = header.
                getFirst("Content-Disposition").split(";");
        for (String filename : contentDisposition) {
            if ((filename.trim().startsWith("filename"))) {
                String[] name = filename.split("=");
                String finalFileName = name[1].trim().replaceAll("\"", "");
                return finalFileName;
            }
        }
        return "";
    }
}