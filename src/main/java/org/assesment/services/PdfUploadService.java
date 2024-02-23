package org.assesment.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MultivaluedMap;
import org.apache.commons.io.IOUtils;
import org.assesment.config.SystemConfig;
import org.assesment.services.exceptions.DuplicatedFileException;
import org.assesment.services.exceptions.FileNotUploadedException;
import org.assesment.services.exceptions.MoreThanOneFileUploadedException;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class PdfUploadService {

    @Inject
    private SystemConfig systemConfig;

    public String uploadFile(MultipartFormDataInput input) {
        List<InputPart> inputParts = extractInputParts(input);

        inputPartsShouldNotBeEmpty(inputParts);
        thereShouldNotBeMoreThanOneFile(inputParts);

        InputPart inputPart = inputParts.get(0);
        String fileName = getFileName(inputPart);
        try {
            InputStream inputStream = inputPart.getBody(InputStream.class, null);
            writeFile(inputStream,fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fileName;
    }

    private boolean isPdfAlreadyInDir(String fileName){
        File file = new File(fileName);
        return file.exists() && !file.isDirectory();
    }

    private void thereShouldNotBeMoreThanOneFile(List<InputPart> inputParts) {
        if (inputParts.size() > 1) {
            throw new MoreThanOneFileUploadedException("More than one file was uploaded");
        }
    }

    private void inputPartsShouldNotBeEmpty(List<InputPart> inputParts) {
        if (inputParts.isEmpty()) {
            throw new FileNotUploadedException("No file could be found in the request");
        }
    }

    private List<InputPart> extractInputParts(MultipartFormDataInput input) {
        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        List<InputPart> inputParts = uploadForm.get("file");
        return inputParts;
    }

    private void writeFile(InputStream inputStream,String fileName)
            throws IOException {
        byte[] bytes = IOUtils.toByteArray(inputStream);
        File customDir = new File(systemConfig.getDirectory());
        fileName = customDir.getAbsolutePath() + File.separator + fileName;
        if (isPdfAlreadyInDir(fileName)){
            throw new DuplicatedFileException("The file "+fileName+" is duplicated ");
        }
        Files.write(Paths.get(fileName), bytes, StandardOpenOption.CREATE_NEW);
    }

    private String getFileName(InputPart inputPart) {
        MultivaluedMap<String, String> header = inputPart.getHeaders();
        String[] contentDisposition = header.getFirst("Content-Disposition").split(";");
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