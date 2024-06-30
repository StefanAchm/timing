package com.asi.timer.controller;

import com.asi.timer.model.view.APICompetitor;
import com.asi.timer.service.CompetitorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/competitor")
public class CompetitorController {

    private final CompetitorService competitorService;

    public CompetitorController(CompetitorService competitorService) {
        this.competitorService = competitorService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createCompetitor(@RequestBody APICompetitor competitorRequest) {
        return ResponseEntity.ok(this.competitorService.createCompetitor(competitorRequest).getId().toString());
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateCompetitor(@RequestBody APICompetitor competitorRequest) {
        return ResponseEntity.ok(this.competitorService.updateCompetitor(competitorRequest).getId().toString());
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteCompetitor(@RequestParam UUID id) {
        return ResponseEntity.ok(this.competitorService.deleteCompetitor(id).getId().toString());
    }

    @GetMapping("/getCompetitors")
    public ResponseEntity<List<APICompetitor>> getCompetitors() {
        return ResponseEntity.ok(this.competitorService.getCompetitors());
    }

    @GetMapping("/generateStartNumber")
    public ResponseEntity<Integer> generateStartNumber() {
        return ResponseEntity.ok(this.competitorService.generateStartNumber());
    }

    @GetMapping("/isStartNumberValid")
    public ResponseEntity<Boolean> isStartNumberValid(
            @RequestParam(required = false) UUID id,
            @RequestParam Integer startNumber) {
        return ResponseEntity.ok(this.competitorService.isStartNumberValid(id, startNumber));
    }

}
