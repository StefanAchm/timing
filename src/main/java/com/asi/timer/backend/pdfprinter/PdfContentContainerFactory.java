package com.asi.timer.backend.pdfprinter;

import com.asi.timer.backend.pdfprinter.model.Columns;
import com.asi.timer.backend.pdfprinter.model.PdfContentContainer;
import com.asi.timer.backend.pdfprinter.model.Rows;
import com.asi.timer.enums.EnumPrintType;
import com.asi.timer.model.db.Competitor;

import java.util.List;

public class PdfContentContainerFactory {

    public static PdfContentContainer getContainer(String folderPath, EnumPrintType type, String gender, int round, List<Competitor> competitors) {
        return switch (type) {
            case START_LIST -> getStartListContainer(folderPath, type, gender, round, competitors);
            case RESULT_LIST -> getResultListContainer(folderPath, type, gender, round, competitors);
        };
    }

    private static PdfContentContainer getResultListContainer(String folderPath, EnumPrintType type, String gender, int round, List<Competitor> competitors) {

        return new PdfContentContainer(
                folderPath,
                getFileName(type, gender, round),
                Columns.getColumns(type),
                type,
                gender,
                round,
                Rows.getRows(type, competitors)
        );

    }

    private static PdfContentContainer getStartListContainer(String folderPath, EnumPrintType type, String gender, int round, List<Competitor> competitors) {

        return new PdfContentContainer(
                folderPath,
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
