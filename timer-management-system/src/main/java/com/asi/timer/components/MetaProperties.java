package com.asi.timer.components;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MetaProperties {

    @Value("${app.eventTitle}")
    private String eventTitle;

    public String getEventTitle() {
        return eventTitle;
    }

    public String getEventTitleWithCurrentYear() {
        return eventTitle + " " + java.time.LocalDate.now().getYear();
    }

}
