package com.asi.timer.controller;

import com.asi.timer.model.view.CompetitorRequest;
import com.asi.timer.service.CompetitorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
