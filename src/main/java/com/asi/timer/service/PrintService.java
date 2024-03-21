package com.asi.timer.service;

import com.asi.timer.enums.EnumPrintType;
import com.asi.timer.model.db.DBCompetitorRound;
import com.asi.timer.repositories.CompetitorRoundRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PrintService {

    private final CompetitorRoundRepository competitorRoundRepository;

    public PrintService(CompetitorRoundRepository competitorRoundRepository) {
        this.competitorRoundRepository = competitorRoundRepository;
    }

    public String print(EnumPrintType printType, UUID competitorRoundId) {

        DBCompetitorRound competitorRound = this.competitorRoundRepository
                .findById(competitorRoundId)
                .orElseThrow(() -> new RuntimeException("CompetitorRound with id " + competitorRoundId + " not found"));

        // return PdfGenerator.generatePdf(); // todo

        return "Not implemented yet";

    }

}
