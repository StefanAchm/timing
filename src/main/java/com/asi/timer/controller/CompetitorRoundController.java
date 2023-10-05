package com.asi.timer.controller;

import com.asi.timer.service.CompetitorRoundService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<String> addCompetitorToRound(@RequestParam UUID competitorRoundID,
                                                       @RequestParam int score) {

        return ResponseEntity.ok(this.competitorRoundService.updateScore(competitorRoundID, score));

    }

}
