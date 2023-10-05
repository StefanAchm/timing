package com.asi.timer.controller;

import com.asi.timer.model.view.RoundRequest;
import com.asi.timer.service.RoundService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/round")
public class RoundController {

    private final RoundService roundService;

    public RoundController(RoundService roundService) {
        this.roundService = roundService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createRound(@RequestBody RoundRequest roundRequest) {
        return ResponseEntity.ok(this.roundService.createRound(roundRequest).getId().toString());
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
