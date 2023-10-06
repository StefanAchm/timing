package com.asi.timer.backend.pdfprinter.model;

import com.asi.timer.enums.EnumPrintType;
import com.asi.timer.model.db.Competitor;

import java.util.List;

public class Rows {

    public static List<Row> getRows(EnumPrintType type, List<Competitor> competitors) {

        return switch (type) {
            case START_LIST -> getStartListRows(competitors);
            case RESULT_LIST -> getResultListRows(competitors);
        };
    }

    public static List<Row> getStartListRows(List<Competitor> competitors) {

        return competitors.stream()
                .map(competitor -> {
                    Row row = new Row();
                    row.setCells(List.of(
                            String.valueOf(competitor.getStartNumber()),
                            competitor.getFullName(),
                            competitor.getCity(),
                            competitor.getClub(),
                            competitor.getDateOfBirthAsString()
                    ));
                    return row;
                })
                .toList();

    }

    public static List<Row> getResultListRows(List<Competitor> competitors) {

        throw new RuntimeException("Not implemented yet");

    }


}
