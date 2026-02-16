package com.kintoh.notifications;

public class ConsoleNotifier implements Notifier {

    public void sendAlert(String resourceType, String name, String ns, String reason, String timeStamp) {
        
        System.out.println("--- DETECCIÓN DE FALLO CRÍTICO ---");
        System.out.println(" Hora:    " + timeStamp);
        System.out.println(" Tipo:    " + resourceType);
        System.out.println(" Recurso: " + name);
        if (ns != null) System.out.println(" NS:      " + ns);
        System.out.println(" Razón:   " + reason);
        System.out.println("----------------------------");
    }
}
