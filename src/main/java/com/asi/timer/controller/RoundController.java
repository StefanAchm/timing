package com.asi.timer.controller;

import com.asi.timer.model.view.CreateRoundRequest;
import com.asi.timer.model.view.RoundRequest;
import com.asi.timer.model.view.RoundResponse;
import com.asi.timer.service.RoundService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/round")
public class RoundController {

    private final RoundService roundService;

    public RoundController(RoundService roundService) {
        this.roundService = roundService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createRound(@RequestParam boolean addCompetitors,
                                              @RequestBody CreateRoundRequest createRoundRequest) {

        return ResponseEntity.ok(this.roundService.createRound(createRoundRequest, addCompetitors).getId().toString());

    }

    @PostMapping("/preview")
    public ResponseEntity<RoundResponse> preview(@RequestBody CreateRoundRequest createRoundRequest) {

        return ResponseEntity.ok(this.roundService.preview(createRoundRequest));

    }

    @PostMapping("/update")
    public ResponseEntity<String> updateRound(@RequestBody RoundRequest roundRequest) {
        return ResponseEntity.ok(this.roundService.updateRound(roundRequest).getId().toString());
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteRound(@RequestBody RoundRequest roundRequest) {
        return ResponseEntity.ok(this.roundService.deleteRound(roundRequest).getId().toString());
    }

    @GetMapping("/getRounds")
    public ResponseEntity<List<RoundResponse>> getRounds() {
        return ResponseEntity.ok(this.roundService.getRounds());
    }


}
