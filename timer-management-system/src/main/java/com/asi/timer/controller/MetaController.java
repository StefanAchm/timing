package com.asi.timer.controller;

import com.asi.timer.components.MetaProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/meta")
public class MetaController {

    private final MetaProperties metaProperties;

    public MetaController(MetaProperties metaProperties) {
        this.metaProperties = metaProperties;
    }

    @GetMapping("/getEventTitle")
    public String getEventTitle() {
        return this.metaProperties.getEventTitleWithCurrentYear();
    }

}
