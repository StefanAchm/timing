package com.asi.timer.controller;

import com.asi.timer.model.view.CompetitorRequest;
import com.asi.timer.model.view.CompetitorResponse;
import com.asi.timer.model.view.RoundRequest;
import com.asi.timer.service.CompetitorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/competitor")
public class CompetitorController {

    private final CompetitorService competitorService;

    public CompetitorController(CompetitorService competitorService) {
        this.competitorService = competitorService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createCompetitor(@RequestBody CompetitorRequest competitorRequest) {
        return ResponseEntity.ok(this.competitorService.createCompetitor(competitorRequest).getId().toString());
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateCompetitor(@RequestBody CompetitorRequest competitorRequest) {
        return ResponseEntity.ok(this.competitorService.updateCompetitor(competitorRequest).getId().toString());
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteCompetitor(@RequestBody CompetitorRequest competitorRequest, @RequestParam boolean soft) {
        return ResponseEntity.ok(this.competitorService.deleteCompetitor(competitorRequest, soft).getId().toString());
    }

    @GetMapping("/getCompetitors")
    public ResponseEntity<List<CompetitorResponse>> getCompetitors() {
        return ResponseEntity.ok(this.competitorService.getCompetitors());
    }

    @GetMapping("/getPossibleCompetitors")
    public ResponseEntity<List<CompetitorResponse>> getPossibleCompetitors(@RequestParam RoundRequest roundRequest) {
        return ResponseEntity.ok(this.competitorService.getPossibleCompetitors(roundRequest));
    }


}
