package com.asi.timer.helper;

import com.asi.timer.backend.CsvUtil;
import com.asi.timer.backend.model.Competitor;
import com.asi.timer.backend.model.CompetitorRound;
import com.asi.timer.backend.model.Round;
import com.asi.timer.enums.EnumGender;

import java.util.List;

public class FileHelper {

    public static List<CompetitorRound> getCompetitorRounds(String folder, List<Competitor> competitors, List<Round> rounds, EnumGender gender) {
        String filePath = folder + "/competitor_rounds.csv";
        List<CompetitorRound> competitorRounds = CsvUtil.readCompetitorRounds(filePath, competitors, rounds);
        competitorRounds = competitorRounds.stream().filter(cr -> cr.getCompetitor().getGender().equals(gender)).toList();
        return competitorRounds;
    }

    public static List<Competitor> getCompetitors(String folder, EnumGender gender) {
        String filePath = folder + "/competitors.csv";
        List<Competitor> competitors = CsvUtil.readCompetitors(filePath);
        competitors = competitors.stream().filter(c -> c.getGender().equals(gender)).toList();
        return competitors;
    }

    public static List<Round> getRounds(String folder, EnumGender gender) {
        String filePath = folder + "/rounds.csv";
        List<Round> rounds = CsvUtil.readRounds(filePath);
        rounds = rounds.stream().filter(r -> r.getGender().equals(gender)).toList();
        return rounds;
    }


}
