package com.kubeVigilant;

import com.kubeVigilant.core.K8sClientFactory;
import com.kubeVigilant.logic.HealthMonitor;
import com.kubeVigilant.watcher.EventWatcher;

import io.kubernetes.client.openapi.apis.CoreV1Api;

public class Main {
    public static void main(String[] args) {
        CoreV1Api api = new K8sClientFactory().createApi();
        HealthMonitor monitor = new HealthMonitor();
        new EventWatcher(api, monitor).start();
    }
}