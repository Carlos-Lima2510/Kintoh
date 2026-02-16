package com.kintoh;

import com.kintoh.core.K8sClientFactory;
import com.kintoh.logic.PodMonitor;
import com.kintoh.watcher.PodWatcher;

import io.kubernetes.client.openapi.apis.CoreV1Api;

public class Main {
    public static void main(String[] args) {
        CoreV1Api api = new K8sClientFactory().createApi();
        PodMonitor monitor = new PodMonitor();
        new PodWatcher(api, monitor).start();
    }
}