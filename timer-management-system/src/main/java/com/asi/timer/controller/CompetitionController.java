package com.asi.timer.controller;

import com.asi.timer.enums.EnumGender;
import com.asi.timer.model.view.APICompetitorRound;
import com.asi.timer.model.view.APICompetitorScore;
import com.asi.timer.model.view.APIRound;
import com.asi.timer.service.CompetitionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PostMapping("/update")
    @PreAuthorize( "hasAnyRole('JUDGE', 'ADMIN')")
    public void updateCurrentRound(@RequestParam(required = false) UUID roundId, @RequestParam(required = false) UUID competitorRoundId) {
        this.competitionService.update(roundId, competitorRoundId);
    }

    @PostMapping("/updateStatus")
    @PreAuthorize("hasAnyRole('JUDGE', 'ADMIN')")
    public void updateStatus(@RequestParam boolean live) {
        this.competitionService.updateStatus(live);
    }

    @GetMapping("/getStatus")
    public ResponseEntity<Boolean> getStatus() {
        return ResponseEntity.ok(this.competitionService.getStatus());
    }

    @GetMapping("/getCurrentCompetitorRounds")
    public ResponseEntity<List<APICompetitorRound>> getCurrentCompetitorRounds() {
        if (!competitionService.getStatus()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(this.competitionService.getCurrentCompetitorRounds());
    }

    @GetMapping("/getCurrentCompetitorRound")
    public ResponseEntity<APICompetitorRound> getCurrentCompetitor() {
        if (!competitionService.getStatus()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(this.competitionService.getCurrentCompetitorRound());
    }

    @GetMapping("/getCurrentRound")
    public ResponseEntity<APIRound> getCurrentRound() {
        if (!competitionService.getStatus()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(this.competitionService.getCurrentRound());
    }

    @GetMapping("/getResultList")
    public ResponseEntity<List<APICompetitorScore>> getResultList(@RequestParam EnumGender gender) {
        if (!competitionService.getStatus()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(this.competitionService.getResultList(gender));
    }

    @GetMapping("/getLatestCompetitorRounds")
    public ResponseEntity<List<APICompetitorRound>> getLatestCompetitorRounds(@RequestParam EnumGender gender) {
        if (!competitionService.getStatus()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(this.competitionService.getLatestCompetitorRounds(gender));
    }

}
