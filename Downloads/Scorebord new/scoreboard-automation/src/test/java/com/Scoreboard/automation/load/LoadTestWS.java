package com.Scoreboard.automation.load;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.time.Duration;
import java.util.concurrent.*;

public class LoadTestWS {

    private static final String WS_URL =
            "wss://dev-scoreboardapi.jaigovinda7.com/socket.io/?EIO=4&transport=websocket";

    private static final int TOTAL_USERS = 3000;
    private static final int THREAD_POOL_SIZE = 300;
    private static final int CONNECT_TIMEOUT_SEC = 15;

    public static void main(String[] args) throws Exception {

        System.setProperty("jdk.httpclient.connectionPoolSize", "0");

        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(CONNECT_TIMEOUT_SEC))
                .build();

        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch connectedLatch = new CountDownLatch(TOTAL_USERS);

        System.out.println("Preparing " + TOTAL_USERS + " users...");

        for (int i = 1; i <= TOTAL_USERS; i++) {
            final int userNo = i;

            executor.submit(() -> {
                try {
                    startLatch.await();

                    client.newWebSocketBuilder()
                            .buildAsync(URI.create(WS_URL), new WebSocket.Listener() {})
                            .thenAccept(ws -> {
                                connectedLatch.countDown();
                                if (userNo % 100 == 0) {
                                    System.out.println("Connected users: " +
                                            (TOTAL_USERS - connectedLatch.getCount()));
                                }
                            })
                            .exceptionally(ex -> {
                                System.err.println("User " + userNo + " failed");
                                return null;
                            })
                            .join();

                } catch (Exception ignored) {}
            });
        }

        System.out.println("Releasing all users AT ONCE...");
        startLatch.countDown();

        connectedLatch.await();
        System.out.println("All users attempted connection.");

        Thread.currentThread().join();
    }
}
