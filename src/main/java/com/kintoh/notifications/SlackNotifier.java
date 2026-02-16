package com.kintoh.notifications;

import java.io.IOException;

import okhttp3.*;

public class SlackNotifier implements Notifier {

    private final String webhookUrl;
    private final OkHttpClient httpClient;

    public static final MediaType JSON_MEDIA_TYPE = MediaType.get("application/json; charset=utf-8");

    public SlackNotifier(String webhookUrl) {
        this.webhookUrl = webhookUrl;
        this.httpClient = new OkHttpClient();
    }

    public void sendAlert(String resourceType, String name, String ns, String reason, String timeStamp) {
        if (webhookUrl == null || webhookUrl.isEmpty()) {
            System.err.println("Slack webhook URL is not configured. Cannot send alert.");
            return;
        }

        String jsonPayload = String.format(
            "{\"text\": \"🚨 *KintohVigilant Alert*\\n" +
            "*Hora:* %s\\n" +
            "*Tipo:* %s\\n" +
            "*Recurso:* `%s` (NS: %s)\\n" +
            "*Error:* *%s*\"}",
            timeStamp, resourceType, name, ns, reason
        );

        RequestBody body = RequestBody.create(jsonPayload, JSON_MEDIA_TYPE);
        Request request = new Request.Builder()
                .url(webhookUrl)
                .post(body)
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            public void onFailure(Call call, IOException e) {
                System.err.println(" --- Error enviando a Slack: " + e.getMessage() + " --- ");
            }

            public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) {
                        System.err.println(" --- Slack rechazó la petición: " + response.code() + " --- ");
                    } else {
                        System.out.println(" --- Notificación enviada a Slack --- ");
                    }
                }
            }
        });
    }
}
