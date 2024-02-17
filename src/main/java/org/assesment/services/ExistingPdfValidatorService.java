package org.assesment.services;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.File;

@ApplicationScoped
public class ExistingPdfValidatorService {

    public boolean isPdfAlreadyInDir(String fileName){
        File file = new File(fileName);
        return file.exists() && !file.isDirectory();
    }

}
