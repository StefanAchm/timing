package com.asi.timer.backend.pdfprinter;

import com.asi.timer.enums.EnumPrintType;
import com.asi.timer.model.db.Competitor;

import java.util.List;

public class PdfContainerFactory {

    private static final String FILEPATH = "C:\\Users\\StefanA\\Desktop\\"; // todo

    public static PdfContainer getContainer(EnumPrintType type, String gender, int round, List<Competitor> competitors) {
        return switch (type) {
            case START_LIST -> getStartListContainer(type, gender, round, competitors);
            case RESULT_LIST -> getResultListContainer(type, gender, round, competitors);
        };
    }

    private static PdfContainer getResultListContainer(EnumPrintType type, String gender, int round, List<Competitor> competitors) {

        return new PdfContainer(
                FILEPATH,
                getFileName(type, gender, round),
                Columns.getColumns(type),
                type,
                gender,
                round,
                Rows.getRows(type, competitors)
        );

    }

    private static PdfContainer getStartListContainer(EnumPrintType type, String gender, int round, List<Competitor> competitors) {

        return new PdfContainer(
                FILEPATH,
                getFileName(type, gender, round),
                Columns.getColumns(type),
                type,
                gender,
                round,
                Rows.getRows(type, competitors)
        );

    }


    private static String getFileName(EnumPrintType type, String gender, int round) {

        String baseName = switch (type) {
            case START_LIST -> "Startliste";
            case RESULT_LIST -> "Rangliste";
        };

        String genderName = switch (gender) {
            case "male" -> "Herren";
            case "female" -> "Damen";
            default -> throw new IllegalStateException("Unexpected value: " + gender);
        };

        switch (type) {
            case START_LIST -> {
                return baseName + "_" + genderName + "_Runde" + round + ".pdf";
            }
            case RESULT_LIST -> {
                return baseName + "_" + genderName + ".pdf";
            }
            default -> throw new IllegalStateException("Unexpected value: " + type);
        }

    }

}
