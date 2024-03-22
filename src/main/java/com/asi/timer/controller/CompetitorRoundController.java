package com.asi.timer.controller;

import com.asi.timer.model.view.APICompetitor;
import com.asi.timer.model.view.APICompetitorRound;
import com.asi.timer.service.CompetitorRoundService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/competitor-round")
public class CompetitorRoundController {

    private final CompetitorRoundService competitorRoundService;

    public CompetitorRoundController(CompetitorRoundService competitorRoundService) {
        this.competitorRoundService = competitorRoundService;
    }

    @PostMapping("/addCompetitorToRound")
    public ResponseEntity<String> addCompetitorToRound(@RequestParam UUID competitorId,
                                                       @RequestParam Integer roundNumber) {

        return ResponseEntity.ok(this.competitorRoundService.addCompetitorToRound(competitorId, roundNumber));

    }

    @PostMapping("/updateScore")
    public ResponseEntity<Double> updateScore(@RequestParam UUID competitorRoundID,
                                              @RequestBody APICompetitorRound competitorRoundRequest) {

        return ResponseEntity.ok(this.competitorRoundService.updateScore(competitorRoundID, competitorRoundRequest));

    }

    @GetMapping("/getCompetitors")
    public ResponseEntity<List<APICompetitor>> getCompetitors(@RequestParam int roundNumber,
                                                              @RequestParam String gender) {

        return ResponseEntity.ok(this.competitorRoundService.getCompetitors(roundNumber, gender));

    }

}
