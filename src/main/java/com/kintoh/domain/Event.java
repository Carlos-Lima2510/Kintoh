package com.kintoh.domain;

import java.time.LocalDateTime;

public class Event {
    private final LocalDateTime timestamp;
    private final String severity;
    private final String message;
    private final Resource resource;

    public Event(String severity, String message, Resource resource) {
        this.timestamp = LocalDateTime.now();
        this.severity = severity;
        this.message = message;
        this.resource = resource;
    }

    public LocalDateTime timestamp() {
        return timestamp;
    }

    public String severity() {
        return severity;
    }

    public String message() {
        return message;
    }

    public Resource resource() {
        return resource;
    }
}
