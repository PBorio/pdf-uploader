package org.assesment.config;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;


@ApplicationScoped
public class SystemConfig {

    @ConfigProperty(name = "upload.directory")
    private String directory;

    public String getDirectory(){
        return directory;
    }

}
