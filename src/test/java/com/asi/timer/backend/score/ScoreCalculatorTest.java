package com.asi.timer.backend.score;

import com.asi.timer.enums.EnumHoldType;
import com.asi.timer.model.db.CompetitorRound;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

class ScoreCalculatorTest {

    private record CompetitorRoundTestInputParams(int holdNumber, EnumHoldType holdType, int tryNumber) {
    }

    private static Stream<Arguments> provideCompetitorRounds() {

        return Stream.of(

                Arguments.of(15.0, new CompetitorRoundTestInputParams(15, EnumHoldType.HELD, 1)),
                Arguments.of(14.99, new CompetitorRoundTestInputParams(15, EnumHoldType.HELD, 2)),
                Arguments.of(14.98, new CompetitorRoundTestInputParams(15, EnumHoldType.HELD, 3)),
                Arguments.of(14.97, new CompetitorRoundTestInputParams(15, EnumHoldType.HELD, 4)),
                Arguments.of(14.96, new CompetitorRoundTestInputParams(15, EnumHoldType.HELD, 5)),
                Arguments.of(14.91, new CompetitorRoundTestInputParams(15, EnumHoldType.HELD, 10)),
                Arguments.of(14.91, new CompetitorRoundTestInputParams(15, EnumHoldType.HELD, 12)),


                Arguments.of(14.60, new CompetitorRoundTestInputParams(15, EnumHoldType.TOUCHED, 1)),
                Arguments.of(14.57, new CompetitorRoundTestInputParams(15, EnumHoldType.TOUCHED, 4)),

                Arguments.of(14.80, new CompetitorRoundTestInputParams(15, EnumHoldType.SLIPPED, 1)),
                Arguments.of(14.77, new CompetitorRoundTestInputParams(15, EnumHoldType.SLIPPED, 4)),

                Arguments.of(15.0, new CompetitorRoundTestInputParams(15, EnumHoldType.HELD, 1)),
                Arguments.of(14.97, new CompetitorRoundTestInputParams(15, EnumHoldType.HELD, 4)),

                Arguments.of(15.20, new CompetitorRoundTestInputParams(15, EnumHoldType.FOLLOWED, 1)),
                Arguments.of(15.17, new CompetitorRoundTestInputParams(15, EnumHoldType.FOLLOWED, 4)),

                Arguments.of(15.40, new CompetitorRoundTestInputParams(15, EnumHoldType.MOVED_ON, 1)),
                Arguments.of(15.37, new CompetitorRoundTestInputParams(15, EnumHoldType.MOVED_ON, 4))

        );

    }

    @ParameterizedTest
    @MethodSource("provideCompetitorRounds")
    void testCalculateScore(ArgumentsAccessor arguments) {

        double expectedScore = arguments.getDouble(0);
        CompetitorRoundTestInputParams competitorRoundTestInputParams = arguments.get(1, CompetitorRoundTestInputParams.class);

        CompetitorRound competitorRound = new CompetitorRound();
        competitorRound.setHoldNumber(competitorRoundTestInputParams.holdNumber());
        competitorRound.setHoldType(competitorRoundTestInputParams.holdType());
        competitorRound.setTryNumber(competitorRoundTestInputParams.tryNumber());


        double actualScore = ScoreCalculator.calculateScore(competitorRound);

        Assertions.assertEquals(expectedScore, actualScore);

    }

    @Test
    void testMultipleRounds() {

        // Competitor 1

        CompetitorRound competitorRound1 = new CompetitorRound();
        competitorRound1.setHoldNumber(15);
        competitorRound1.setHoldType(EnumHoldType.TOUCHED);
        competitorRound1.setTryNumber(1);

        CompetitorRound competitorRound2 = new CompetitorRound();
        competitorRound2.setHoldNumber(15);
        competitorRound2.setHoldType(EnumHoldType.HELD);
        competitorRound2.setTryNumber(1);

        // Competitor 2

        CompetitorRound competitorRound3 = new CompetitorRound();
        competitorRound3.setHoldNumber(15);
        competitorRound3.setHoldType(EnumHoldType.HELD);
        competitorRound3.setTryNumber(1);

        CompetitorRound competitorRound4 = new CompetitorRound();
        competitorRound4.setHoldNumber(15);
        competitorRound4.setHoldType(EnumHoldType.TOUCHED);
        competitorRound4.setTryNumber(1);

        double scoreCompetitor1 = ScoreCalculator.calculateScore(List.of(competitorRound1, competitorRound2));
        double scoreCompetitor2 = ScoreCalculator.calculateScore(List.of(competitorRound3, competitorRound4));

        Assertions.assertTrue(scoreCompetitor1 > scoreCompetitor2,
                "Competitor 1 {" + scoreCompetitor1 + "} should have a higher score than competitor 2 {" + scoreCompetitor2 + "}");


    }


}