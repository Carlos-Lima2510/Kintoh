package com.kubeVigilant.watcher;

import io.kubernetes.client.openapi.models.V1Pod;

public interface PodMonitor {
    void onEvent(V1Pod pod, String eventType);
}
