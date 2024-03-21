package com.asi.timer.backend.pdfprinter;

import com.asi.timer.backend.pdfprinter.model.Columns;
import com.asi.timer.backend.pdfprinter.model.PdfContentContainer;
import com.asi.timer.backend.pdfprinter.model.Rows;
import com.asi.timer.backend.score.model.CompetitorScore;
import com.asi.timer.enums.EnumPrintType;
import com.asi.timer.model.db.DBCompetitor;

import java.time.LocalDate;
import java.util.List;

public class PdfContentContainerFactory {

    public static PdfContentContainer getContainer(String folderPath,
                                                   String eventTitle,
                                                   EnumPrintType type,
                                                   String gender,
                                                   int round,
                                                   LocalDate date,
                                                   List<DBCompetitor> competitors,
                                                   List<CompetitorScore> competitorScores) {
        return switch (type) {
            case START_LIST -> getStartListContainer(folderPath, eventTitle, type, gender, round, date, competitors);
            case RESULT_LIST -> getResultListContainer(folderPath, eventTitle, type, gender, round, date, competitorScores);
        };
    }

    private static PdfContentContainer getResultListContainer(String folderPath,
                                                              String eventTile,
                                                              EnumPrintType type,
                                                              String gender,
                                                              int round,
                                                              LocalDate date,
                                                              List<CompetitorScore> competitorScores) {

        return new PdfContentContainer(
                folderPath,
                getFileName(type, gender, round),
                eventTile,
                date,
                Columns.getColumns(type),
                type,
                gender,
                round,
                Rows.getResultListRows(competitorScores)
        );

    }

    private static PdfContentContainer getStartListContainer(String folderPath,
                                                             String eventTile,
                                                             EnumPrintType type,
                                                             String gender,
                                                             int round,
                                                             LocalDate date,
                                                             List<DBCompetitor> competitors) {

        return new PdfContentContainer(
                folderPath,
                getFileName(type, gender, round),
                eventTile,
                date,
                Columns.getColumns(type),
                type,
                gender,
                round,
                Rows.getStartListRows(competitors)
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
                return baseName + "_" + genderName + "_Runde" + round;
            }
            case RESULT_LIST -> {
                return baseName + "_" + genderName;
            }
            default -> throw new IllegalStateException("Unexpected value: " + type);
        }

    }

}
