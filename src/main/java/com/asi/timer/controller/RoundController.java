package com.asi.timer.controller;

import com.asi.timer.model.view.RoundRequest;
import com.asi.timer.service.RoundService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/round")
public class RoundController {

    private final RoundService roundService;

    public RoundController(RoundService roundService) {
        this.roundService = roundService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createRound(@RequestParam boolean addCompetitors,
                                              @RequestBody RoundRequest roundRequest) {

        return ResponseEntity.ok(this.roundService.createRound(roundRequest, addCompetitors).getId().toString());

    }

    @PostMapping("/updateSuccessScore")
    public ResponseEntity<String> updateSuccessScore(@RequestParam UUID roundId,
                                                     @RequestParam int successScore) {

        return ResponseEntity.ok(this.roundService.updateSuccessScore(roundId, successScore).getId().toString());

    }

    @PostMapping("/update")
    public ResponseEntity<String> updateRound(@RequestBody RoundRequest roundRequest) {
        return ResponseEntity.ok(this.roundService.updateRound(roundRequest).getId().toString());
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteRound(@RequestBody RoundRequest roundRequest) {
        return ResponseEntity.ok(this.roundService.deleteRound(roundRequest).getId().toString());
    }

}
