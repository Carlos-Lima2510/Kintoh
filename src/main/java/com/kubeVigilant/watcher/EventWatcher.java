package com.kubeVigilant.watcher;

import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.util.Watch;
import com.google.gson.reflect.TypeToken;

import okhttp3.Call;

public class EventWatcher {
    private final CoreV1Api api;
    private final PodMonitor monitor;

    public EventWatcher(CoreV1Api api, PodMonitor monitor) {
        this.api = api;
        this.monitor = monitor;
    }

    public void start() {
        System.out.println("Vigilancia Activa. Esperando Eventos...");

        try {
            Watch<V1Pod> watch = Watch.createWatch(
                api.getApiClient(),
                createWatchCall(),
                new TypeToken<Watch.Response<V1Pod>>(){}.getType()
            );

            for (Watch.Response<V1Pod> item : watch) {
                if (item.object != null) {
                    monitor.onEvent(item.object, item.type);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("El Watcher ha fallado inesperadamente", e);
        }
    }

    private Call createWatchCall() throws Exception {
        return api.listPodForAllNamespacesCall(
            null, null, null, null, null, null, null, null, null, null,
            Boolean.TRUE,
            null
        );
    }
}