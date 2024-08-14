package com.asi.timer.backend.utils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class FileUtils {

    private FileUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static void openFile(File file) {
        if(Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().open(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static File createFileOrUnique(String filePath, String fileName, String fileEnding) {

        File file = new File(filePath, fileName + "." + fileEnding);

        if(file.exists()) {
            UUID uuid = UUID.randomUUID();
            String newFileName = fileName + "_" + uuid + "." + fileEnding;
            file = new File(filePath, newFileName);
        }

        return file;

    }

    public static String readFileFromResources(String fileName) {
        ClassLoader classLoader = FileUtils.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);
        if(inputStream == null) {
            throw new IllegalArgumentException("File not found: " + fileName);
        }
        try {
            return new String(inputStream.readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
