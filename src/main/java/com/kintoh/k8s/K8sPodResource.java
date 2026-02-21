package com.kintoh.k8s;

import com.kintoh.domain.Resource;

import io.kubernetes.client.openapi.models.V1Pod;

public class K8sPodResource implements Resource {

    private final V1Pod pod;

    public K8sPodResource(V1Pod pod) {
        this.pod = pod;
    }

    public String name() {
        return pod.getMetadata() != null ? pod.getMetadata().getName() : "Desconocido";
    }

    public String namespace() {
        return pod.getMetadata() != null ? pod.getMetadata().getNamespace() : "Desconocido";
    }
    
    public V1Pod rawPod() {
        return pod;
    }
}
