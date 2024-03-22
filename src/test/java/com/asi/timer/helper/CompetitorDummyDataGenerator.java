package com.asi.timer.helper;

import com.asi.timer.enums.EnumGender;
import com.asi.timer.model.db.DBCompetitor;

import java.time.LocalDate;

public class CompetitorDummyDataGenerator {

    public static DBCompetitor generateCompetitor(int startNumber,
                                                  String firstName,
                                                  String lastName,
                                                  String city,
                                                  String club,
                                                  String dateOfBirth,
                                                  EnumGender gender) {

        DBCompetitor competitor = new DBCompetitor();

        competitor.setStartNumber(startNumber);
        competitor.setFirstName(firstName);
        competitor.setLastName(lastName);
        competitor.setCity(city);
        competitor.setClub(club);
        competitor.setDateOfBirth(LocalDate.parse(dateOfBirth));
        competitor.setGender(gender);

        return competitor;

    }

}
