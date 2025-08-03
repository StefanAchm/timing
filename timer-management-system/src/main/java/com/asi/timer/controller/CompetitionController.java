package com.asi.timer.controller;

import com.asi.timer.enums.EnumGender;
import com.asi.timer.model.view.APICompetitorRound;
import com.asi.timer.model.view.APICompetitorScore;
import com.asi.timer.model.view.APIRound;
import com.asi.timer.service.CompetitionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/competition")
public class CompetitionController {

    private final CompetitionService competitionService;

    public CompetitionController(CompetitionService competitionService) {
        this.competitionService = competitionService;
    }

    @PostMapping("/updateCurrentRound")
    public void updateCurrentRound(@RequestParam UUID roundId) {
        this.competitionService.updateCurrentRound(roundId);
    }

    @PostMapping("/updateCurrentCompetitorRound")
    public void updateCurrentCompetitorRound(@RequestParam UUID competitorRoundId) {
        this.competitionService.updateCurrentCompetitorRound(competitorRoundId);
    }

    @GetMapping("/getCurrentCompetitorRounds")
    public ResponseEntity<List<APICompetitorRound>> getCurrentCompetitorRounds() {
        return ResponseEntity.ok(this.competitionService.getCurrentCompetitorRounds());
    }

    @GetMapping("/getCurrentCompetitorRound")
    public ResponseEntity<APICompetitorRound> getCurrentCompetitor() {
        return ResponseEntity.ok(this.competitionService.getCurrentCompetitorRound());
    }

    @GetMapping("/getCurrentRound")
    public ResponseEntity<APIRound> getCurrentRound() {
        return ResponseEntity.ok(this.competitionService.getCurrentRound());
    }

    @GetMapping("/getResultList")
    public ResponseEntity<List<APICompetitorScore>> getResultList(@RequestParam EnumGender gender) {
        return ResponseEntity.ok(this.competitionService.getResultList(gender));
    }

    @GetMapping("/getLatestCompetitorRounds")
    public ResponseEntity<List<APICompetitorRound>> getLatestCompetitorRounds(@RequestParam EnumGender gender) {
        return ResponseEntity.ok(this.competitionService.getLatestCompetitorRounds(gender));
    }

}
