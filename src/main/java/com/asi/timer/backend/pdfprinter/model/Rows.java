package com.asi.timer.backend.pdfprinter.model;

import com.asi.timer.backend.score.model.CompetitorScore;
import com.asi.timer.enums.EnumCompetitorRoundStatus;
import com.asi.timer.model.db.DBCompetitor;

import java.util.ArrayList;
import java.util.List;

public class Rows {

    public static List<Row> getStartListRows(List<DBCompetitor> competitors) {

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
                    "Runde " + score.getLastRound().getRound().getRoundNumber(),
                    holdNumber,
                    holdType,
                    tryNumber,
                    points
            ));

            Row apply = row;
            list.add(apply);
        }

        return list;

    }


}
