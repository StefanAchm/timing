package com.asi.timer.controller;

import com.asi.timer.model.view.APIRound;
import com.asi.timer.service.RoundService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/round")
public class RoundController {

    private final RoundService roundService;

    public RoundController(RoundService roundService) {
        this.roundService = roundService;
    }

    @PostMapping("/create")
    public ResponseEntity<APIRound> createRound(@RequestParam boolean addCompetitors,
                                              @RequestBody APIRound createRoundRequest) {

        return ResponseEntity.ok(this.roundService.createRound(createRoundRequest, addCompetitors));

    }

    @PostMapping("/preview")
    public ResponseEntity<APIRound> preview(@RequestBody APIRound createRoundRequest) {

        return ResponseEntity.ok(this.roundService.preview(createRoundRequest));

    }

    @PostMapping("/update")
    public ResponseEntity<String> updateRound(@RequestBody APIRound roundRequest) {
        return ResponseEntity.ok(this.roundService.updateRound(roundRequest).getId().toString());
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteRound(@RequestParam UUID id) {
        return ResponseEntity.ok(this.roundService.deleteRound(id).getId().toString());
    }

    @GetMapping("/getRounds")
    public ResponseEntity<List<APIRound>> getRounds() {
        return ResponseEntity.ok(this.roundService.getRounds());
    }


}
