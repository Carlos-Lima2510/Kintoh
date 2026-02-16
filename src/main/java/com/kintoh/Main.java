package com.kintoh;

import com.kintoh.core.K8sClientFactory;
import com.kintoh.logic.PodMonitor;
import com.kintoh.notifications.ConsoleNotifier;
import com.kintoh.notifications.SlackNotifier;
import com.kintoh.watcher.PodWatcher;

import io.kubernetes.client.openapi.apis.CoreV1Api;

public class Main {
    public static void main(String[] args) {
        System.out.println(" --- Iniciando KintohVigilant... --- ");

        CoreV1Api api = new K8sClientFactory().createApi();
        PodMonitor monitor = new PodMonitor();

        monitor.addNotifier(new ConsoleNotifier());

        String slackUrl = System.getenv("SLACK_WEBHOOK_URL");
        if (slackUrl != null && !slackUrl.isEmpty()) {
            monitor.addNotifier(new SlackNotifier(slackUrl));
            System.out.println("✅ Notificaciones de Slack activadas.");
        } else {
            System.out.println("ℹ️ Variable SLACK_WEBHOOK_URL no encontrada. Slack desactivado.");
        }

        new Thread(() -> new PodWatcher(api, monitor).start()).start();
    }
}