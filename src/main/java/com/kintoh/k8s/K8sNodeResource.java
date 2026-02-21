package com.kintoh.k8s;

import com.kintoh.domain.Resource;

import io.kubernetes.client.openapi.models.V1Node;

public class K8sNodeResource implements Resource {
    private final V1Node node;

    public K8sNodeResource(V1Node node) {
        this.node = node;
    }

    public String name() {
        return node.getMetadata() != null ? node.getMetadata().getName() : "Desconocido";
    }

    public V1Node rawNode() {
        return node;
    }

    public String namespace() {
        return "cluster-scope";
    }
}
