package com.kintoh.notifiers;

import com.kintoh.domain.Event;
import com.kintoh.domain.Notifier;
import java.time.format.DateTimeFormatter;

public class ConsoleNotifier implements Notifier {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void send(Event event) {
        System.out.println("\n--------------------------------------------------");
        System.out.println(" [" + event.severity() + "] DETECCIÓN DE ANOMALÍA ");
        System.out.println("    * Hora: " + event.timestamp().format(FORMATTER));
        System.out.println("    * Recurso:  " + event.resource().name());
        System.out.println("    * Ámbito:   " + event.resource().namespace());
        System.out.println("    * Info: " + event.message());
        System.out.println("--------------------------------------------------");
    }
}