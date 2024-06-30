package com.asi.timer.backend.score;

import com.asi.timer.backend.model.CompetitorRound;
import com.asi.timer.backend.model.Round;
import com.asi.timer.backend.utils.ScoreUtil;
import com.asi.timer.enums.EnumCompetitorRoundStatus;
import com.asi.timer.enums.EnumHoldType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

class ScoreUtilTest {

    private record Mr(List<Ct> ct1, List<Ct> ct2, boolean firstShouldWin) {

        List<CompetitorRound> toCompetitorRounds1() {
            return ct1.stream().map(Ct::toCompetitorRound).toList();
        }

        List<CompetitorRound> toCompetitorRounds2() {
            return ct2.stream().map(Ct::toCompetitorRound).toList();
        }

    }

    // Wrapper for competitor round test input params
    private record Ct(int holdNumber, EnumHoldType holdType, int tryNumber, int roundNumber) {

        Ct(int holdNumber, EnumHoldType holdType, int tryNumber) {
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

                Arguments.of(15.0, new Ct(15, EnumHoldType.HELD, 1)),
                Arguments.of(14.99, new Ct(15, EnumHoldType.HELD, 2)),
                Arguments.of(14.98, new Ct(15, EnumHoldType.HELD, 3)),
                Arguments.of(14.97, new Ct(15, EnumHoldType.HELD, 4)),
                Arguments.of(14.96, new Ct(15, EnumHoldType.HELD, 5)),
                Arguments.of(14.91, new Ct(15, EnumHoldType.HELD, 10)),
                Arguments.of(14.91, new Ct(15, EnumHoldType.HELD, 12)),


                Arguments.of(14.60, new Ct(15, EnumHoldType.TOUCHED, 1)),
                Arguments.of(14.57, new Ct(15, EnumHoldType.TOUCHED, 4)),

                Arguments.of(14.80, new Ct(15, EnumHoldType.SLIPPED, 1)),
                Arguments.of(14.77, new Ct(15, EnumHoldType.SLIPPED, 4)),

                Arguments.of(15.0, new Ct(15, EnumHoldType.HELD, 1)),
                Arguments.of(14.97, new Ct(15, EnumHoldType.HELD, 4)),

                Arguments.of(15.20, new Ct(15, EnumHoldType.FOLLOWED, 1)),
                Arguments.of(15.17, new Ct(15, EnumHoldType.FOLLOWED, 4)),

                Arguments.of(15.40, new Ct(15, EnumHoldType.MOVED_ON, 1)),
                Arguments.of(15.37, new Ct(15, EnumHoldType.MOVED_ON, 4))

        );

    }

    @ParameterizedTest
    @MethodSource("provideCompetitorRounds")
    void testCalculateTotalScore(double expectedScore, Ct ct) {

        double actualScore = ScoreUtil.calculateScore(ct.toCompetitorRound());

        Assertions.assertEquals(expectedScore, actualScore);

    }

    private static Stream<Arguments> provideCompetitorRoundsForMultipleRounds() {

        return Stream.of(

                Arguments.of(
                        new Mr(
                                List.of(
                                        new Ct(15, EnumHoldType.TOUCHED, 1, 1),
                                        new Ct(15, EnumHoldType.HELD, 1, 2)
                                ),
                                List.of(
                                        new Ct(15, EnumHoldType.HELD, 1, 1),
                                        new Ct(15, EnumHoldType.TOUCHED, 1, 2)
                                ),
                                true
                        )
                ),

                Arguments.of(
                        new Mr(
                                List.of(
                                        new Ct(15, EnumHoldType.TOUCHED, 1, 1),
                                        new Ct(15, EnumHoldType.HELD, 1, 2)
                                ),
                                List.of(
                                        new Ct(15, EnumHoldType.TOUCHED, 1, 1),
                                        new Ct(15, EnumHoldType.TOUCHED, 1, 2)
                                ),
                                true
                        )
                ),

                Arguments.of(
                        new Mr(
                                List.of(
                                        new Ct(15, EnumHoldType.HELD, 1, 1),
                                        new Ct(15, EnumHoldType.HELD, 1, 2)
                                ),
                                List.of(
                                        new Ct(15, EnumHoldType.TOUCHED, 1, 1),
                                        new Ct(15, EnumHoldType.TOUCHED, 1, 2)
                                ),
                                true
                        )

                ),

                Arguments.of(
                        new Mr(
                                List.of(
                                        new Ct(15, EnumHoldType.HELD, 1, 1),
                                        new Ct(15, EnumHoldType.TOUCHED, 1, 2)
                                ),
                                List.of(
                                        new Ct(15, EnumHoldType.TOUCHED, 1, 1),
                                        new Ct(15, EnumHoldType.HELD, 1, 2)
                                ),
                                false
                        )

                ),

                Arguments.of(
                        new Mr(
                                List.of(
                                        new Ct(15, EnumHoldType.HELD, 1, 1),
                                        new Ct(15, EnumHoldType.HELD, 1, 2)
                                ),
                                List.of(
                                        new Ct(15, EnumHoldType.TOUCHED, 1, 1),
                                        new Ct(15, EnumHoldType.HELD, 1, 2)
                                ),
                                true
                        )

                )


        );

    }

    @ParameterizedTest
    @MethodSource("provideCompetitorRoundsForMultipleRounds")
    void testMultipleRounds(ArgumentsAccessor arguments) {

        Mr mr = arguments.get(0, Mr.class);

        List<Round> rounds = new ArrayList<>();
        rounds.add(new Round(1, 30));
        rounds.add(new Round(2, 30));

        double scoreCompetitor1 = ScoreUtil.calculateTotalScore(mr.toCompetitorRounds1(), rounds);
        double scoreCompetitor2 = ScoreUtil.calculateTotalScore(mr.toCompetitorRounds2(), rounds);

        System.out.println("scoreCompetitor1: " + scoreCompetitor1);
        System.out.println("scoreCompetitor2: " + scoreCompetitor2);

        if (mr.firstShouldWin()) {

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