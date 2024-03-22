package com.asi.timer.backend.pdfprinter.utils;

import com.asi.timer.backend.pdfprinter.enums.EnumGender;
import com.asi.timer.enums.EnumPrintType;

public class FileNameGenerator {


    public static String getFileName(EnumPrintType type, EnumGender gender, int round) {

        String baseName = switch (type) {
            case START_LIST -> "Startliste";
            case RESULT_LIST -> "Rangliste";
        };

        String genderName = switch (gender) {
            case HERREN -> "Herren";
            case DAMEN -> "Damen";
        };

        switch (type) {
            case START_LIST -> {
                return baseName + "_" + genderName + "_Runde" + round;
            }
            case RESULT_LIST -> {
                return baseName + "_" + genderName;
            }
            default -> throw new IllegalStateException("Unexpected value: " + type);
        }

    }

}
