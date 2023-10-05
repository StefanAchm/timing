package com.asi.timer.controller;

import com.asi.timer.model.db.Competitor;
import com.asi.timer.model.view.CompetitorResponse;
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
                                                       @RequestParam UUID roundId) {

        return ResponseEntity.ok(this.competitorRoundService.addCompetitorToRound(competitorId, roundId));

    }

    @PostMapping("/updateScore")
    public ResponseEntity<String> updateScore(@RequestParam UUID competitorRoundID,
                                              @RequestParam int score) {

        return ResponseEntity.ok(this.competitorRoundService.updateScore(competitorRoundID, score));

    }

    @GetMapping("/getCompetitors")
    public ResponseEntity<List<CompetitorResponse>> getCompetitors(@RequestParam int roundNumber,
                                                                   @RequestParam String gender) {

        return ResponseEntity.ok(this.competitorRoundService.getCompetitors(roundNumber, gender));

    }

}
