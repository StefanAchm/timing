package com.asi.timer.backend.pdfprinter.model;

import com.asi.timer.backend.score.model.CompetitorScore;
import com.asi.timer.model.db.DBCompetitor;

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

        return competitorScores.stream()
                .map(competitorScore -> {

                    Row row = new Row();
                    row.setCells(List.of(
                            String.valueOf(competitorScore.getRank()),
                            competitorScore.getCompetitor().getFullName(),
                            competitorScore.getCompetitor().getCity(),
                            competitorScore.getCompetitor().getClub(),
                            competitorScore.getCompetitor().getDateOfBirthAsString(),
                            "Runde " + competitorScore.getLastRound().getRound().getRoundNumber(),
                            "15", // todo
                            "weitergezogen", // todo
                            "5", // todo
                            String.valueOf(competitorScore.getScoreForPdf())
                    ));

                    return row;

                })
                .toList();

    }


}
