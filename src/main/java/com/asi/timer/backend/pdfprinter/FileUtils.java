package com.asi.timer.backend.pdfprinter;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class FileUtils {

    public static void createFolderIfNotExists(String folderPath) {
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
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
}
