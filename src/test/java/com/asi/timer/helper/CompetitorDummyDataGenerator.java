package com.asi.timer.helper;

import com.asi.timer.model.db.Competitor;

import java.time.LocalDate;

public class CompetitorDummyDataGenerator {

    public static Competitor generateCompetitor(int startNumber,
                                                String firstName,
                                                String lastName,
                                                String city,
                                                String club,
                                                String dateOfBirth,
                                                String gender) {

        Competitor competitor = new Competitor();

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
