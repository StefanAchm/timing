package com.asi.timer.backend.pdfprinter.model;

import com.asi.timer.enums.EnumPrintType;

import java.util.List;

public class Columns {

    public static List<Column> getColumns(EnumPrintType printType) {

        return switch (printType) {
            case START_LIST -> getStartListHeader();
            case RESULT_LIST -> getResultListHeader();
        };

    }

    public static List<Column> getStartListHeader() {

        return List.of(
                new Column("Startnummer", 2),
                new Column("Name", 6),
                new Column("Wohnort", 3),
                new Column("Verein", 3),
                new Column("Geburtsdatum", 3)
        );

    }

    public static List<Column> getResultListHeader() {

        return List.of(
                new Column("Rang", 1.5),
                new Column("Name", 5),
                new Column("Wohnort", 3),
                new Column("Verein", 3),
                new Column("Geburtsdatum", 4),
                new Column("Runde", 2.5),
                new Column("Griff", 2),
                new Column("Faktor", 3),
                new Column("Versuch", 2),
                new Column("Punkte", 2.5)
        );

    }

}
