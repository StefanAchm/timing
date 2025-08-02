package com.asi.timer.controller;

import com.asi.timer.model.view.APIStatistics;
import com.asi.timer.repositories.CompetitorRepository;
import com.asi.timer.repositories.CompetitorRoundRepository;
import com.asi.timer.repositories.RoundRepository;
import com.asi.timer.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @DeleteMapping("/deleteAllData")
    public void deleteAllData() {
        this.adminService.deleteAllData();
    }

    @GetMapping("/getStatistics")
    public ResponseEntity<APIStatistics> getStatistics() {
        return ResponseEntity.ok(this.adminService.getStatistics());
    }

}
