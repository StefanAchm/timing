package com.asi.timer.helper;

import com.asi.timer.backend.model.Competitor;
import com.asi.timer.backend.model.CompetitorRound;
import com.asi.timer.backend.model.CompetitorScore;
import com.asi.timer.enums.EnumCompetitorRoundStatus;
import com.asi.timer.enums.EnumHoldType;

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
        return generateCompetitorScore(
                i,
                CompetitorDummyData.competitorX(i),
                3,
                100- i
        );
    }

    public static CompetitorScore competitorScore1 = generateCompetitorScore(
            1,
            CompetitorDummyData.competitor1,
            1,
            15.9999999999999876
    );

    public static CompetitorScore competitorScore2 = generateCompetitorScore(
            2,
            CompetitorDummyData.competitor3,
            1,
            15
    );

    public static CompetitorScore competitorScore3 = generateCompetitorScore(
            3,
            CompetitorDummyData.competitor4,
            1,
            14
    );

    public static CompetitorScore competitorScore4 = generateCompetitorScore(
            4,
            CompetitorDummyData.competitorWithLongName1,
            1,
            13
    );

    public static CompetitorScore competitorScore5 = generateCompetitorScore(
            5,
            CompetitorDummyData.competitorWithLongClubname1,
            1,
            12
    );

    public static CompetitorScore competitorScore6 = generateCompetitorScore(
            6,
            CompetitorDummyData.competitorWithLongClubname2,
            1,
            11
    );

    public static CompetitorScore competitorScore7 = generateCompetitorScore(
            7,
            CompetitorDummyData.competitorWithLongClubname3,
            1,
            10.999885522
    );


    public static CompetitorScore competitorScore8 = generateCompetitorScore(
            8,
            CompetitorDummyData.competitorWithLongClubname4,
            1,
            9.16
    );



    public static CompetitorScore competitorScore9 = generateCompetitorScore(
            9,
            CompetitorDummyData.competitorWithLongCityName,
            1,
            8.0346
    );


    public static CompetitorScore generateCompetitorScore(
            int rank,
            Competitor competitor,
            int roundNumber,
            double score) {

        CompetitorRound lastRound = new CompetitorRound();
        lastRound.setRoundNumber(roundNumber);
        lastRound.setCompetitor(competitor);
        lastRound.setCompetitorRoundStatus(EnumCompetitorRoundStatus.COMPLETED);
        lastRound.setHoldNumber(25);
        lastRound.setHoldType(EnumHoldType.HELD);
        lastRound.setTryNumber(3);

        CompetitorScore competitorScore = new CompetitorScore();
        competitorScore.setRank(rank);
        competitorScore.setCompetitor(competitor);
        competitorScore.setLastRound(lastRound);
        competitorScore.setScore(score);

        return competitorScore;

    }


}
