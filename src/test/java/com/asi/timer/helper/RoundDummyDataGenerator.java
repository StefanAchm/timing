package com.asi.timer.helper;

import com.asi.timer.model.db.Round;

public class RoundDummyDataGenerator {

    public static Round generateRound(int roundNumber, String gender) {

        Round round = new Round();
        round.setRoundNumber(roundNumber);
        round.setGender(gender);

        return round;

    }

}
