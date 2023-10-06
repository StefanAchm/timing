package com.asi.timer.helper;

import com.asi.timer.model.db.Competitor;

public class CompetitorDummyData {

    public static Competitor competitor1 = CompetitorDummyDataGenerator.generateCompetitor(
            1,
            "Max",
            "Mustermann",
            "Musterstadt",
            "Musterclub",
            "1990-01-01",
            "male"
    );

    public static Competitor competitor2 = CompetitorDummyDataGenerator.generateCompetitor(
            2,
            "Maxine",
            "Musterfrau",
            "Musterstadt",
            "Musterclub",
            "1990-01-01",
            "female"
    );

    public static Competitor competitor3 = CompetitorDummyDataGenerator.generateCompetitor(
            3,
            "Maximilian",
            "Muster",
            "Musterstadt",
            "Musterclub",
            "1990-01-01",
            "male"
    );

    public static Competitor competitor4 = CompetitorDummyDataGenerator.generateCompetitor(
            4,
            "Maximilian",
            "Muster",
            "Musterstadt",
            "Musterclub",
            "1990-01-01",
            "male"
    );

}
