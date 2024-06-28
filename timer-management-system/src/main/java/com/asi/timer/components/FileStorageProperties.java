package com.asi.timer.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class FileStorageProperties {

    private static final Logger logger = LoggerFactory.getLogger(FileStorageProperties.class);

    @Value("${app.file.dir}")
    private String dir;


    @PostConstruct
    public void init() {

        Path rootLocation = Paths.get(dir);

        try {

            Path directories = Files.createDirectories(rootLocation);

            String absolutePath = directories.toAbsolutePath().toString();

            logger.info("Upload directory created: {}", absolutePath);

        } catch (Exception e) {

            throw new RuntimeException("Could not create upload directory!");

        }

    }

    public String getUploadDir() {
        return dir;
    }

}
