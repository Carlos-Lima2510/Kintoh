package com.kubeVigilant.watcher;

import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.util.Watch;

import java.util.concurrent.atomic.AtomicBoolean;

import com.google.gson.reflect.TypeToken;

import okhttp3.Call;

public class EventWatcher {
    private final CoreV1Api api;
    private final PodMonitor monitor;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private static final int MAX_RETRIES = 5;
    private int currentRetries = 0;

    public EventWatcher(CoreV1Api api, PodMonitor monitor) {
        this.api = api;
        this.monitor = monitor;
    }

    public void start() {
        System.out.println("Vigilancia Activa. Esperando Eventos...");
        running.set(true);

        while (running.get()) {
            performWatch();

            if (running.get()) {
                if (currentRetries >= MAX_RETRIES) {
                    System.err.println(" --- FATAL: Límite de reintentos alcanzado (" + MAX_RETRIES + "). Apagando. --- ");
                    stopWatcher();
                } else {
                    applyBackoff();
                    currentRetries++;
                    System.out.println(" --- Intento de reconexión " + currentRetries + "/" + MAX_RETRIES + " --- ");
                }
            }
        }
        
        System.out.println("--- El monitor se ha detenido correctamente --- ");
    }

    private void stopWatcher() {
        running.set(false);
    }

    private void applyBackoff() {
        try {
            System.out.println(" --- Reintentando conexión en 5 segundos... --- ");
            Thread.sleep(5000);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            stopWatcher();
        }
    }

    private void performWatch() {
        try {
            Watch<V1Pod> watch = Watch.createWatch(
                    api.getApiClient(),
                    createWatchCall(),
                    new TypeToken<Watch.Response<V1Pod>>() {
                    }.getType());

            if (currentRetries > 0) {
                System.out.println(" --- Conexión recuperada - Reiniciando contador de intentos. ---");
                currentRetries = 0;
            }

            for (Watch.Response<V1Pod> item : watch) {
                if (!running.get()) {
                    return;
                }

                if (item.object != null) {
                    monitor.onEvent(item.object, item.type);
                }
            }
        } catch (Exception e) {
            System.err.println(" --- Error en la conexión: " + e.getMessage() + " --- ");
        }
    }

    private Call createWatchCall() throws Exception {
        return api.listPodForAllNamespacesCall(
                null, null, null, null, null, null, null, null, null, null,
                Boolean.TRUE,
                null);
    }
}