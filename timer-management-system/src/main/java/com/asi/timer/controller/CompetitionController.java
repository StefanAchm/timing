package com.asi.timer.controller;

import com.asi.timer.enums.EnumGender;
import com.asi.timer.model.view.APICompetitor;
import com.asi.timer.model.view.APICompetitorRound;
import com.asi.timer.model.view.APICompetitorScore;
import com.asi.timer.model.view.APIRound;
import com.asi.timer.service.CompetitionService;
import com.asi.timer.service.CompetitorService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/competition")
public class CompetitionController {

    private final CompetitionService competitionService;
    private final CompetitorService competitorService;

    public CompetitionController(CompetitionService competitionService, CompetitorService competitorService) {
        this.competitionService = competitionService;
        this.competitorService = competitorService;
    }

    @PostMapping("/update")
    @PreAuthorize( "hasAnyRole('JUDGE', 'ADMIN')")
    public void updateCurrentRound(@RequestParam(required = false) UUID roundId, @RequestParam(required = false) UUID competitorRoundId) {
        this.competitionService.update(roundId, competitorRoundId);
    }

    @PostMapping("/updateLiveStatus")
    @PreAuthorize("hasAnyRole('JUDGE', 'ADMIN')")
    public void updateLiveStatus(@RequestParam boolean live) {
        this.competitionService.updateLiveStatus(live);
    }

    @PostMapping("/updateRegistrationStatus")
    @PreAuthorize("hasAnyRole('JUDGE', 'ADMIN')")
    public void updateRegistrationStatus(@RequestParam boolean registrationOpen) {
        this.competitionService.updateRegistrationStatus(registrationOpen);
    }


    @GetMapping("/getLiveStatus")
    public ResponseEntity<Boolean> getLiveStatus() {
        return ResponseEntity.ok(this.competitionService.getLiveStatus());
    }

    @GetMapping("/getRegistrationStatus")
    public ResponseEntity<Boolean> getRegistrationStatus() {
        return ResponseEntity.ok(this.competitionService.getRegistrationStatus());
    }



    @GetMapping("/getCurrentCompetitorRounds")
    public ResponseEntity<List<APICompetitorRound>> getCurrentCompetitorRounds() {
        if (!competitionService.getLiveStatus()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(this.competitionService.getCurrentCompetitorRounds());
    }

    @GetMapping("/getCurrentCompetitorRound")
    public ResponseEntity<APICompetitorRound> getCurrentCompetitor() {
        if (!competitionService.getLiveStatus()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(this.competitionService.getCurrentCompetitorRound());
    }

    @GetMapping("/getCurrentRound")
    public ResponseEntity<APIRound> getCurrentRound() {
        if (!competitionService.getLiveStatus()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(this.competitionService.getCurrentRound());
    }

    @GetMapping("/getResultList")
    public ResponseEntity<List<APICompetitorScore>> getResultList(@RequestParam EnumGender gender) {
        if (!competitionService.getLiveStatus()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(this.competitionService.getResultList(gender));
    }

    @GetMapping("/getLatestCompetitorRounds")
    public ResponseEntity<List<APICompetitorRound>> getLatestCompetitorRounds(@RequestParam EnumGender gender) {
        if (!competitionService.getLiveStatus()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(this.competitionService.getLatestCompetitorRounds(gender));
    }

    @PostMapping("/register")
    public ResponseEntity<APICompetitor> registerCompetitor(@RequestBody APICompetitor competitorRequest) {
        if (!competitionService.getRegistrationStatus()) {
            return ResponseEntity.badRequest().build();
        }
        try {
            return ResponseEntity.ok(this.competitorService.registerCompetitor(competitorRequest));
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            return ResponseEntity.status(409).build();
        }
    }

}
