package io.kowalski.cronnark.services;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.kowalski.cronnark.models.SlackWebhookPayload;
import io.kowalski.cronnark.models.TrackedTask;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Singleton
@Slf4j
public class SlackService {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private final ExecutorService executor;

    @Inject
    public SlackService() {
        executor = Executors.newFixedThreadPool(5);
    }

    public void sendAWOLMessages(final Set<TrackedTask> tasks) {
        tasks.forEach(task -> {
            executor.submit(() -> {
                try {
                    final SlackWebhookPayload swp = SlackWebhookPayload.builder()
                            .text(task.getName().concat(" has not responded in the specified interval of ")
                                    .concat(task.getInterval().name()).concat(" It has last checked in at ")
                                    .concat(task.getLastCheckInLDT().toString()))
                            .build();
                    final ObjectMapper mapper = new ObjectMapper();
                    final OkHttpClient client = new OkHttpClient();
                    final RequestBody body = RequestBody.create(JSON, mapper.writeValueAsString(swp));
                    final Request request = new Request.Builder().url(task.getWebhook()).post(body).build();
                    final Response response = client.newCall(request).execute();
                    response.close();
                } catch (final IOException e) {
                    log.error("Unable to send message to Slack Webhook", e);
                }
            });
        });
    }

}
