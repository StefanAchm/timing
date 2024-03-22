package com.asi.timer.helper;

import com.asi.timer.model.db.DBRound;

public class RoundDummyDataGenerator {

    public static DBRound generateRound(int roundNumber, String gender) {

        DBRound round = new DBRound();
        round.setRoundNumber(roundNumber);
        round.setGender(gender);

        return round;

    }

}
