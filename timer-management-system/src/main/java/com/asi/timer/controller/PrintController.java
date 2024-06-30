package com.asi.timer.controller;

import com.asi.timer.enums.EnumGender;
import com.asi.timer.service.PrintService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/print")
public class PrintController {

    private final PrintService printService;

    public PrintController(PrintService printService) {
        this.printService = printService;
    }

    @GetMapping("/startList")
    public ResponseEntity<Resource> getStartList(@RequestParam UUID id) {

        ByteArrayResource resource = printService.getStartList(id);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);

    }

    @GetMapping("/resultList")
    public ResponseEntity<Resource> getResultList(@RequestParam EnumGender gender) {

        ByteArrayResource resource = printService.getResultList(gender);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);

    }

}
