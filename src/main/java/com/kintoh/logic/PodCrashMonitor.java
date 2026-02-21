package com.kintoh.logic;

import com.kintoh.domain.Event;
import com.kintoh.domain.Monitor;
import com.kintoh.domain.Resource;
import com.kintoh.k8s.K8sPodResource;
import io.kubernetes.client.openapi.models.V1ContainerState;
import io.kubernetes.client.openapi.models.V1ContainerStatus;
import io.kubernetes.client.openapi.models.V1Pod;

import java.util.List;
import java.util.Optional;

public class PodCrashMonitor implements Monitor {
    private static final String ERROR_REASON = "CrashLoopBackOff";

    public Optional<Event> check(Resource resource) {
        if (!(resource instanceof K8sPodResource)) {
            return Optional.empty();
        }

        V1Pod pod = ((K8sPodResource) resource).rawPod();
        
        if (pod.getStatus() == null || pod.getStatus().getContainerStatuses() == null) {
            return Optional.empty();
        }

        List<V1ContainerStatus> statuses = pod.getStatus().getContainerStatuses();
        for (V1ContainerStatus status : statuses) {
            V1ContainerState state = status.getState();
            if (state != null && state.getWaiting() != null && ERROR_REASON.equals(state.getWaiting().getReason())) {
                String msg = "El contenedor '" + status.getName() + "' ha entrado en estado " + ERROR_REASON;
                return Optional.of(new Event("CRÍTICO", msg, resource));
            }
        }
        return Optional.empty();
    }
}