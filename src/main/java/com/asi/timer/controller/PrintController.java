package com.asi.timer.controller;

import com.asi.timer.enums.EnumPrintType;
import com.asi.timer.service.PrintService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/print")
public class PrintController {

    private final PrintService printService;

    public PrintController(PrintService printService) {
        this.printService = printService;
    }

    @PostMapping("/print")
    public ResponseEntity<String> print(@RequestParam EnumPrintType printType,
                                        @RequestParam UUID id) {

        return ResponseEntity.ok(this.printService.print(printType, id));

    }


}
