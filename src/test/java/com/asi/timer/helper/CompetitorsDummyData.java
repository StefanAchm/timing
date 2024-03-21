package com.asi.timer.helper;

import com.asi.timer.model.db.DBCompetitor;

import java.util.List;

public class CompetitorsDummyData {

    public static List<DBCompetitor> getCompetitors() {

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

    public static List<DBCompetitor> geCompetitorsWithLongStrings() {

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


}
