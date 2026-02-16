package com.kintoh.notifications;

public interface Notifier {
    void sendAlert(
        String resourceType, 
        String resourceName, 
        String namespace,    
        String reason,      
        String timeStamp
    );
}
