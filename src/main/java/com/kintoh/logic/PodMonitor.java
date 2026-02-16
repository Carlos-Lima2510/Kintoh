package com.kintoh.logic;

import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1ContainerState;
import io.kubernetes.client.openapi.models.V1ContainerStatus;

import java.util.ArrayList;
import java.util.List;

import com.kintoh.watcher.Monitor;
import com.kintoh.notifications.Notifier;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PodMonitor implements Monitor<V1Pod> {

    private final List<Notifier> notifiers = new ArrayList<>();

    private static final String ERROR_REASON = "CrashLoopBackOff";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void onEvent(V1Pod pod, String eventType) {
        V1ContainerStatus status = getPodCrashing(pod);
        if (status != null) {
            dispatchAlerts(pod, status);
        }
    }

    public void addNotifier(Notifier notifier) {
        notifiers.add(notifier);
    }

    private V1ContainerStatus getPodCrashing(V1Pod pod) {
        if (pod.getStatus() == null || pod.getStatus().getContainerStatuses() == null) {
            return null;
        }

        List<V1ContainerStatus> statuses = pod.getStatus().getContainerStatuses();
        
        for (V1ContainerStatus status : statuses) {
            V1ContainerState state = status.getState();
            if (state != null 
                && state.getWaiting() != null 
                && ERROR_REASON.equals(state.getWaiting().getReason())) {
                return status;
            }
        }
        return null;
    }

    private void dispatchAlerts(V1Pod pod, V1ContainerStatus container) {
        String namespace = pod.getMetadata().getNamespace();
        String podName = pod.getMetadata().getName();
        String reason = container.getState().getWaiting().getReason();
        String timestamp = LocalDateTime.now().format(DATE_FORMATTER);

        for (Notifier notifier : notifiers) {
            try {
                notifier.sendAlert(pod.getKind(), podName, namespace, reason, timestamp);
            } catch (Exception e) {
                System.err.println("Error enviando notificación: " + e.getMessage());
            }
        }
    }
}