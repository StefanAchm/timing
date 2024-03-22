package com.asi.timer.helper;

import com.asi.timer.model.db.DBCompetitor;

public class CompetitorDummyData {

    public static DBCompetitor competitor1 = CompetitorDummyDataGenerator.generateCompetitor(
            1,
            "Max",
            "Mustermann",
            "Musterstadt",
            "Musterclub",
            "1990-01-01",
            "male"
    );

    public static DBCompetitor competitor2 = CompetitorDummyDataGenerator.generateCompetitor(
            2,
            "Maxine",
            "Musterfrau",
            "Musterstadt",
            "Musterclub",
            "1990-01-01",
            "female"
    );

    public static DBCompetitor competitor3 = CompetitorDummyDataGenerator.generateCompetitor(
            3,
            "Maximilian",
            "Muster",
            "Musterstadt",
            "Musterclub",
            "1990-01-01",
            "male"
    );

    public static DBCompetitor competitor4 = CompetitorDummyDataGenerator.generateCompetitor(
            4,
            "Maximilian",
            "Muster",
            "Musterstadt",
            "Musterclub",
            "1990-01-01",
            "male"
    );

    public static DBCompetitor competitorWithLongName1 = CompetitorDummyDataGenerator.generateCompetitor(
            5,
            "Maximilian",
            "ABCDEFGHIJKLMNOPQ",
            "Musterstadt",
            "Musterclub",
            "1990-01-01",
            "male"
    );

    public static DBCompetitor competitorWithLongClubname1 = CompetitorDummyDataGenerator.generateCompetitor(
            6,
            "Maximilian",
            "Mustermann",
            "Musterstadt",
            "AV - ABCDEFGHIJKL",
            "1990-01-01",
            "male"
    );

    public static DBCompetitor competitorWithLongClubname2 = CompetitorDummyDataGenerator.generateCompetitor(
            7,
            "Maximilian",
            "Mustermann",
            "Musterstadt",
            "ABC ABCDEFGHI",
            "1990-01-01",
            "male"
    );

    public static DBCompetitor competitorWithLongClubname3 = CompetitorDummyDataGenerator.generateCompetitor(
            8,
            "Maximilian",
            "Mustermann",
            "Musterstadt",
            "Kletterverein ABCDEFGHI",
            "1990-01-01",
            "male"
    );

    public static DBCompetitor competitorWithLongClubname4 = CompetitorDummyDataGenerator.generateCompetitor(
            9,
            "Maximilian",
            "Mustermann",
            "Musterstadt",
            "ÖAV ABCDEFGHI",
            "1990-01-01",
            "male"
    );

    public static DBCompetitor competitorWithLongCityName = CompetitorDummyDataGenerator.generateCompetitor(
            10,
            "Maximilian",
            "Mustermann",
            "ABCDEFGHIJKLMNOPQRST",
            "ÖAV ABCDEFGHI",
            "1990-01-01",
            "male"
    );

    public static DBCompetitor competitorX(int startNumber) {

        return CompetitorDummyDataGenerator.generateCompetitor(
                startNumber,
                "Max",
                "Mustermann",
                "Musterstadt",
                "Musterclub",
                "1990-01-01",
                "male"
        );

    }

}
