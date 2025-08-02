package com.asi.timer.backend.score;

import com.asi.timer.backend.model.Competitor;
import com.asi.timer.backend.model.CompetitorRound;
import com.asi.timer.backend.model.Round;
import com.asi.timer.backend.utils.ScoreUtil;
import com.asi.timer.enums.EnumCompetitorRoundStatus;
import com.asi.timer.enums.EnumGender;
import com.asi.timer.enums.EnumHoldType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

class ScoreUtilTest {

    private record MultipleRoundsWithTwoCompetitors(
            List<CompetitorRoundTestData> competitorRoundTestData1,
            List<CompetitorRoundTestData> competitorRoundTestData2,
            boolean firstShouldWin
    ) {

        private List<CompetitorRound> toCompetitorRounds(List<CompetitorRoundTestData> competitorRoundTestDataList) {

            Competitor competitor = new Competitor(); // Set a new Competitor instance
            competitor.setId(UUID.randomUUID());
            competitor.setGender(EnumGender.HERREN);

            return competitorRoundTestDataList
                    .stream()
                    .map(competitorRoundTestData -> {
                        CompetitorRound competitorRound = competitorRoundTestData.toCompetitorRound();
                        competitorRound.setCompetitor(competitor);
                        return competitorRound;
                    })
                    .toList();
        }

        List<CompetitorRound> toCompetitorRounds1() {
            return toCompetitorRounds(competitorRoundTestData1);
        }

        List<CompetitorRound> toCompetitorRounds2() {
            return toCompetitorRounds(competitorRoundTestData2);
        }

    }

    // Wrapper for competitor round test input params
    private record CompetitorRoundTestData(int holdNumber, EnumHoldType holdType, int tryNumber, int roundNumber) {

        CompetitorRoundTestData(int holdNumber, EnumHoldType holdType, int tryNumber) {
            this(holdNumber, holdType, tryNumber, 1);
        }

        CompetitorRound toCompetitorRound() {
            CompetitorRound competitorRound = new CompetitorRound();
            competitorRound.setHoldNumber(holdNumber);
            competitorRound.setHoldType(holdType);
            competitorRound.setTryNumber(tryNumber);
            competitorRound.setRoundNumber(roundNumber);
            competitorRound.setCompetitorRoundStatus(EnumCompetitorRoundStatus.COMPLETED);
            return competitorRound;
        }

    }

    private static Stream<Arguments> provideCompetitorRounds() {

        return Stream.of(

                Arguments.of(15.0, new CompetitorRoundTestData(15, EnumHoldType.HELD, 1)),
                Arguments.of(14.99, new CompetitorRoundTestData(15, EnumHoldType.HELD, 2)),
                Arguments.of(14.98, new CompetitorRoundTestData(15, EnumHoldType.HELD, 3)),
                Arguments.of(14.97, new CompetitorRoundTestData(15, EnumHoldType.HELD, 4)),
                Arguments.of(14.96, new CompetitorRoundTestData(15, EnumHoldType.HELD, 5)),
                Arguments.of(14.91, new CompetitorRoundTestData(15, EnumHoldType.HELD, 10)),
                Arguments.of(14.91, new CompetitorRoundTestData(15, EnumHoldType.HELD, 12)),


                Arguments.of(14.60, new CompetitorRoundTestData(15, EnumHoldType.TOUCHED, 1)),
                Arguments.of(14.57, new CompetitorRoundTestData(15, EnumHoldType.TOUCHED, 4)),

                Arguments.of(14.80, new CompetitorRoundTestData(15, EnumHoldType.SLIPPED, 1)),
                Arguments.of(14.77, new CompetitorRoundTestData(15, EnumHoldType.SLIPPED, 4)),

                Arguments.of(15.0, new CompetitorRoundTestData(15, EnumHoldType.HELD, 1)),
                Arguments.of(14.97, new CompetitorRoundTestData(15, EnumHoldType.HELD, 4)),

                Arguments.of(15.20, new CompetitorRoundTestData(15, EnumHoldType.FOLLOWED, 1)),
                Arguments.of(15.17, new CompetitorRoundTestData(15, EnumHoldType.FOLLOWED, 4)),

                Arguments.of(15.40, new CompetitorRoundTestData(15, EnumHoldType.MOVED_ON, 1)),
                Arguments.of(15.37, new CompetitorRoundTestData(15, EnumHoldType.MOVED_ON, 4))

        );

    }

    @ParameterizedTest
    @MethodSource("provideCompetitorRounds")
    void testCalculateScoreOfAllRounds(double expectedScore, CompetitorRoundTestData competitorRoundTestData) {

        double actualScore = ScoreUtil.calculateScoreOfRound(competitorRoundTestData.toCompetitorRound());

        Assertions.assertEquals(expectedScore, actualScore);

    }

    private static Stream<Arguments> provideCompetitorRoundsForMultipleRounds() {

        return Stream.of(

                Arguments.of(
                        new MultipleRoundsWithTwoCompetitors(
                                List.of(
                                        new CompetitorRoundTestData(15, EnumHoldType.TOUCHED, 1, 1),
                                        new CompetitorRoundTestData(15, EnumHoldType.HELD, 1, 2)
                                ),
                                List.of(
                                        new CompetitorRoundTestData(15, EnumHoldType.HELD, 1, 1),
                                        new CompetitorRoundTestData(15, EnumHoldType.TOUCHED, 1, 2)
                                ),
                                true
                        )
                ),

                Arguments.of(
                        new MultipleRoundsWithTwoCompetitors(
                                List.of(
                                        new CompetitorRoundTestData(15, EnumHoldType.TOUCHED, 1, 1),
                                        new CompetitorRoundTestData(15, EnumHoldType.HELD, 1, 2)
                                ),
                                List.of(
                                        new CompetitorRoundTestData(15, EnumHoldType.TOUCHED, 1, 1),
                                        new CompetitorRoundTestData(15, EnumHoldType.TOUCHED, 1, 2)
                                ),
                                true
                        )
                ),

                Arguments.of(
                        new MultipleRoundsWithTwoCompetitors(
                                List.of(
                                        new CompetitorRoundTestData(15, EnumHoldType.HELD, 1, 1),
                                        new CompetitorRoundTestData(15, EnumHoldType.HELD, 1, 2)
                                ),
                                List.of(
                                        new CompetitorRoundTestData(15, EnumHoldType.TOUCHED, 1, 1),
                                        new CompetitorRoundTestData(15, EnumHoldType.TOUCHED, 1, 2)
                                ),
                                true
                        )

                ),

                Arguments.of(
                        new MultipleRoundsWithTwoCompetitors(
                                List.of(
                                        new CompetitorRoundTestData(15, EnumHoldType.HELD, 1, 1),
                                        new CompetitorRoundTestData(15, EnumHoldType.TOUCHED, 1, 2)
                                ),
                                List.of(
                                        new CompetitorRoundTestData(15, EnumHoldType.TOUCHED, 1, 1),
                                        new CompetitorRoundTestData(15, EnumHoldType.HELD, 1, 2)
                                ),
                                false
                        )

                ),

                // TODO: That is not working at the moment!
                Arguments.of(
                        new MultipleRoundsWithTwoCompetitors(
                                List.of(
                                        new CompetitorRoundTestData(15, EnumHoldType.HELD, 1, 1),
                                        new CompetitorRoundTestData(15, EnumHoldType.HELD, 1, 2)
                                ),
                                List.of(
                                        new CompetitorRoundTestData(15, EnumHoldType.TOUCHED, 1, 1),
                                        new CompetitorRoundTestData(15, EnumHoldType.HELD, 1, 2)
                                ),
                                true
                        )

                )


        );

    }

    @ParameterizedTest
    @MethodSource("provideCompetitorRoundsForMultipleRounds")
    void testMultipleRounds(ArgumentsAccessor arguments) {

        MultipleRoundsWithTwoCompetitors multipleRoundsWithTwoCompetitors = arguments.get(0, MultipleRoundsWithTwoCompetitors.class);

        List<Round> rounds = new ArrayList<>();
        rounds.add(new Round(1, 30, EnumGender.HERREN));
        rounds.add(new Round(2, 30, EnumGender.HERREN));

        double scoreCompetitor1 = ScoreUtil.calculateScoreOfAllRounds(multipleRoundsWithTwoCompetitors.toCompetitorRounds1(), rounds).getTotalScore();
        double scoreCompetitor2 = ScoreUtil.calculateScoreOfAllRounds(multipleRoundsWithTwoCompetitors.toCompetitorRounds2(), rounds).getTotalScore();

        System.out.println("scoreCompetitor1: " + scoreCompetitor1);
        System.out.println("scoreCompetitor2: " + scoreCompetitor2);

        if (multipleRoundsWithTwoCompetitors.firstShouldWin()) {

            Assertions.assertTrue(
                    scoreCompetitor1 > scoreCompetitor2,
                    "Competitor 1 {" + scoreCompetitor1 + "} should have a higher score than competitor 2 {" + scoreCompetitor2 + "}");

        } else {
            Assertions.assertTrue(
                    scoreCompetitor2 > scoreCompetitor1,
                    "Competitor 2 {" + scoreCompetitor2 + "} should have a higher score than competitor 1 {" + scoreCompetitor1 + "}");
        }

    }

}