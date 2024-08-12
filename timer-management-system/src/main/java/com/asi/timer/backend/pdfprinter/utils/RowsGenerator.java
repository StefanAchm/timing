package com.asi.timer.backend.pdfprinter.utils;

import com.asi.timer.backend.model.Competitor;
import com.asi.timer.backend.model.CompetitorScore;
import com.asi.timer.backend.pdfprinter.model.Cell;
import com.asi.timer.backend.pdfprinter.model.Row;
import com.asi.timer.enums.EnumCompetitorRoundStatus;
import com.asi.timer.enums.EnumPrintType;

import java.util.ArrayList;
import java.util.List;

public class RowsGenerator {

    public static List<Row> getRows(EnumPrintType printType, List<Competitor> competitors, List<CompetitorScore> competitorScores) {

        return switch (printType) {
            case START_LIST -> getStartListRows(competitors);
            case RESULT_LIST -> getResultListRows(competitorScores);
        };

    }


    public static List<Row> getStartListRows(List<Competitor> competitors) {

        List<Row> list = new ArrayList<>();

        for (int i = 0; i < competitors.size(); i++) {

            int currentStartNumber = i + 1;

            Competitor competitor1 = competitors.get(i);

            Row row = new Row();
            row.setCells(
                    List.of(
                            new Cell(currentStartNumber + " (" + competitor1.getStartNumber() + ")"),
                            new Cell(competitor1.getFullName()),
                            new Cell(competitor1.getCity()),
                            new Cell(competitor1.getClub()),
                            new Cell(competitor1.getDateOfBirthAsString())
                    )
            );

            list.add(row);

        }

        return list;

    }

    public static List<Row> getResultListRows(List<CompetitorScore> competitorScores) {

        List<Row> list = new ArrayList<>();

        for (CompetitorScore score : competitorScores) {

            String holdNumber = score.getLastRound().getCompetitorRoundStatus().equals(EnumCompetitorRoundStatus.COMPLETED)
                    ? String.valueOf(score.getLastRound().getHoldNumber())
                    : "";

            String holdType = score.getLastRound().getCompetitorRoundStatus().equals(EnumCompetitorRoundStatus.COMPLETED)
                    ? score.getLastRound().getHoldType().toString()
                    : "";

            String tryNumber = score.getLastRound().getCompetitorRoundStatus().equals(EnumCompetitorRoundStatus.COMPLETED)
                    ? String.valueOf(score.getLastRound().getTryNumber())
                    : "";

            String points = score.getLastRound().getCompetitorRoundStatus().equals(EnumCompetitorRoundStatus.COMPLETED)
                    ? String.valueOf(score.getScoreForPdf())
                    : "DNF";

            Row row = new Row();
            row.setCells(
                    List.of(
                            new Cell(String.valueOf(score.getRank())),
                            new Cell(score.getCompetitor().getFullName(), score.getCompetitor().getCity() + " (" + score.getCompetitor().getClub() + ")"),
                            new Cell(score.getCompetitor().getDateOfBirthAsString()),
                            new Cell("Runde " + score.getLastRound().getRoundNumber()),
                            new Cell(holdNumber),
                            new Cell(holdType),
                            new Cell(tryNumber),
                            new Cell(points)
                    )
            );

            list.add(row);

        }

        return list;

    }


}
