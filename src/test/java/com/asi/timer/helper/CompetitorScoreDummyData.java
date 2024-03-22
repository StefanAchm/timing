package com.asi.timer.helper;

import com.asi.timer.backend.score.model.CompetitorScore;
import com.asi.timer.enums.EnumGender;

import java.util.ArrayList;
import java.util.List;

public class CompetitorScoreDummyData {

    public static List<CompetitorScore> getMale1() {

        return List.of(
                CompetitorScoreDummyData.competitorScore1,
                CompetitorScoreDummyData.competitorScore2,
                CompetitorScoreDummyData.competitorScore3,
                CompetitorScoreDummyData.competitorScore4,
                CompetitorScoreDummyData.competitorScore5,
                CompetitorScoreDummyData.competitorScore6,
                CompetitorScoreDummyData.competitorScore7,
                CompetitorScoreDummyData.competitorScore8,
                CompetitorScoreDummyData.competitorScore9
        );

    }

    public static List<CompetitorScore> getMale(int nrOfCompetitors) {

        List<CompetitorScore> competitorScores = new ArrayList<>();

        for(int i = 1; i <= nrOfCompetitors; i++) {
            competitorScores.add(CompetitorScoreDummyData.competitorScoreX(i));
        }

        return competitorScores;

    }

    private static CompetitorScore competitorScoreX(int i) {
        return CompetitorScoreDummyDataGenerator.generateCompetitorScore(
                i,
                CompetitorDummyData.competitorX(i),
                RoundDummyDataGenerator.generateRound(3, EnumGender.HERREN),
                100- i
        );
    }

    public static CompetitorScore competitorScore1 = CompetitorScoreDummyDataGenerator.generateCompetitorScore(
            1,
            CompetitorDummyData.competitor1,
            RoundDummyDataGenerator.generateRound(1, EnumGender.HERREN),
            15.9999999999999876
    );

    public static CompetitorScore competitorScore2 = CompetitorScoreDummyDataGenerator.generateCompetitorScore(
            2,
            CompetitorDummyData.competitor3,
            RoundDummyDataGenerator.generateRound(1, EnumGender.HERREN),
            15
    );

    public static CompetitorScore competitorScore3 = CompetitorScoreDummyDataGenerator.generateCompetitorScore(
            3,
            CompetitorDummyData.competitor4,
            RoundDummyDataGenerator.generateRound(1, EnumGender.HERREN),
            14
    );

    public static CompetitorScore competitorScore4 = CompetitorScoreDummyDataGenerator.generateCompetitorScore(
            4,
            CompetitorDummyData.competitorWithLongName1,
            RoundDummyDataGenerator.generateRound(1, EnumGender.HERREN),
            13
    );

    public static CompetitorScore competitorScore5 = CompetitorScoreDummyDataGenerator.generateCompetitorScore(
            5,
            CompetitorDummyData.competitorWithLongClubname1,
            RoundDummyDataGenerator.generateRound(1, EnumGender.HERREN),
            12
    );

    public static CompetitorScore competitorScore6 = CompetitorScoreDummyDataGenerator.generateCompetitorScore(
            6,
            CompetitorDummyData.competitorWithLongClubname2,
            RoundDummyDataGenerator.generateRound(1, EnumGender.HERREN),
            11
    );

    public static CompetitorScore competitorScore7 = CompetitorScoreDummyDataGenerator.generateCompetitorScore(
            7,
            CompetitorDummyData.competitorWithLongClubname3,
            RoundDummyDataGenerator.generateRound(1, EnumGender.HERREN),
            10.999885522
    );


    public static CompetitorScore competitorScore8 = CompetitorScoreDummyDataGenerator.generateCompetitorScore(
            8,
            CompetitorDummyData.competitorWithLongClubname4,
            RoundDummyDataGenerator.generateRound(1, EnumGender.HERREN),
            9.16
    );



    public static CompetitorScore competitorScore9 = CompetitorScoreDummyDataGenerator.generateCompetitorScore(
            9,
            CompetitorDummyData.competitorWithLongCityName,
            RoundDummyDataGenerator.generateRound(1, EnumGender.HERREN),
            8.0346
    );




}
