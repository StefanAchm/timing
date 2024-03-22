package com.asi.timer.helper;

import com.asi.timer.backend.model.Competitor;
import com.asi.timer.enums.EnumGender;

import java.time.LocalDate;
import java.util.List;

public class CompetitorDummyData {

    public static Competitor competitor1 = generateCompetitor(
            1,
            "Max",
            "Mustermann",
            "Musterstadt",
            "Musterclub",
            "1990-01-01",
            EnumGender.HERREN
    );

    public static Competitor competitor2 = generateCompetitor(
            2,
            "Maxine",
            "Musterfrau",
            "Musterstadt",
            "Musterclub",
            "1990-01-01",
            EnumGender.DAMEN
    );

    public static Competitor competitor3 = generateCompetitor(
            3,
            "Maximilian",
            "Muster",
            "Musterstadt",
            "Musterclub",
            "1990-01-01",
            EnumGender.HERREN
    );

    public static Competitor competitor4 = generateCompetitor(
            4,
            "Maximilian",
            "Muster",
            "Musterstadt",
            "Musterclub",
            "1990-01-01",
            EnumGender.HERREN
    );

    public static Competitor competitorWithLongName1 = generateCompetitor(
            5,
            "Maximilian",
            "ABCDEFGHIJKLMNOPQ",
            "Musterstadt",
            "Musterclub",
            "1990-01-01",
            EnumGender.HERREN
    );

    public static Competitor competitorWithLongClubname1 = generateCompetitor(
            6,
            "Maximilian",
            "Mustermann",
            "Musterstadt",
            "AV - ABCDEFGHIJKL",
            "1990-01-01",
            EnumGender.HERREN
    );

    public static Competitor competitorWithLongClubname2 = generateCompetitor(
            7,
            "Maximilian",
            "Mustermann",
            "Musterstadt",
            "ABC ABCDEFGHI",
            "1990-01-01",
            EnumGender.HERREN
    );

    public static Competitor competitorWithLongClubname3 = generateCompetitor(
            8,
            "Maximilian",
            "Mustermann",
            "Musterstadt",
            "Kletterverein ABCDEFGHI",
            "1990-01-01",
            EnumGender.HERREN
    );

    public static Competitor competitorWithLongClubname4 = generateCompetitor(
            9,
            "Maximilian",
            "Mustermann",
            "Musterstadt",
            "ÖAV ABCDEFGHI",
            "1990-01-01",
            EnumGender.HERREN
    );

    public static Competitor competitorWithLongCityName = generateCompetitor(
            10,
            "Maximilian",
            "Mustermann",
            "ABCDEFGHIJKLMNOPQRST",
            "ÖAV ABCDEFGHI",
            "1990-01-01",
            EnumGender.HERREN
    );

    public static Competitor competitorX(int startNumber) {

        return generateCompetitor(
                startNumber,
                "Max",
                "Mustermann",
                "Musterstadt",
                "Musterclub",
                "1990-01-01",
                EnumGender.HERREN
        );

    }

    public static List<Competitor> getCompetitors() {

        return List.of(
                CompetitorDummyData.competitor1,
                CompetitorDummyData.competitor1,
                CompetitorDummyData.competitor1,
                CompetitorDummyData.competitor1,
                CompetitorDummyData.competitor1,
                CompetitorDummyData.competitor1,
                CompetitorDummyData.competitor1,
                CompetitorDummyData.competitor2,
                CompetitorDummyData.competitor2,
                CompetitorDummyData.competitor2,
                CompetitorDummyData.competitor2,
                CompetitorDummyData.competitor3,
                CompetitorDummyData.competitor3,
                CompetitorDummyData.competitor3,
                CompetitorDummyData.competitor3,
                CompetitorDummyData.competitor3,
                CompetitorDummyData.competitor3,
                CompetitorDummyData.competitor3,
                CompetitorDummyData.competitor4,
                CompetitorDummyData.competitor4,
                CompetitorDummyData.competitor4,
                CompetitorDummyData.competitor4,
                CompetitorDummyData.competitor4,
                CompetitorDummyData.competitor4,
                CompetitorDummyData.competitor4,
                CompetitorDummyData.competitor4
        );

    }

    public static List<Competitor> geCompetitorsWithLongStrings() {

        return List.of(

                CompetitorDummyData.competitor1,
                CompetitorDummyData.competitor2,
                CompetitorDummyData.competitor3,
                CompetitorDummyData.competitor4,
                CompetitorDummyData.competitorWithLongName1,
                CompetitorDummyData.competitorWithLongClubname1,
                CompetitorDummyData.competitorWithLongClubname2,
                CompetitorDummyData.competitorWithLongClubname3,
                CompetitorDummyData.competitorWithLongClubname4,
                CompetitorDummyData.competitorWithLongCityName,
                CompetitorDummyData.competitorX(11),
                CompetitorDummyData.competitorX(12),
                CompetitorDummyData.competitorX(13),
                CompetitorDummyData.competitorX(14),
                CompetitorDummyData.competitorX(15),
                CompetitorDummyData.competitorX(16)

        );


    }

    private static Competitor generateCompetitor(int startNumber,
                                                String firstName,
                                                String lastName,
                                                String city,
                                                String club,
                                                String dateOfBirth,
                                                EnumGender gender) {

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
