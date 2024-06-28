package com.asi.timer.backend.pdfprinter.utils;

import com.asi.timer.backend.model.Competitor;
import com.asi.timer.backend.pdfprinter.model.Row;
import com.asi.timer.backend.model.CompetitorScore;
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
            row.setCells(List.of(

                    currentStartNumber + " (" + competitor1.getStartNumber() + ")",
                    competitor1.getFullName(),
                    competitor1.getCity(),
                    competitor1.getClub(),
                    competitor1.getDateOfBirthAsString()
            ));

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
            row.setCells(List.of(
                    String.valueOf(score.getRank()),
                    score.getCompetitor().getFullName(),
                    score.getCompetitor().getCity(),
                    score.getCompetitor().getClub(),
                    score.getCompetitor().getDateOfBirthAsString(),
                    "Runde " + score.getLastRound().getRoundNumber(),
                    holdNumber,
                    holdType,
                    tryNumber,
                    points
            ));

            list.add(row);
        }

        return list;

    }


}
