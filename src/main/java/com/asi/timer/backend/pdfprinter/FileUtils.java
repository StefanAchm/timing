package com.asi.timer.backend.pdfprinter;

import java.io.File;

public class FileUtils {

    public static void createFolderIfNotExists(String folderPath) {
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

}
