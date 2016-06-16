package net.puklo.disco;

import com.google.gson.Gson;
import jodd.http.HttpRequest;
import jodd.http.HttpResponse;
import net.puklo.disco.model.AppReg;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;
import static spark.Spark.awaitInitialization;
import static spark.Spark.get;
import static spark.Spark.port;

public class ClientApp {

    private static final String endpoint = "http://localhost:2001/v1/apps";

    public static void main(final String[] args) {
        final String name = System.getProperty("app.name", "ClientApp");
        final Integer port = Integer.valueOf(System.getProperty("app.port"));

        final Gson gson = new Gson();

        final Map<String, String> attribs = new HashMap<>();
        attribs.put("foo", "bar");

        final AppReg reg = new AppReg(name, "1.0." + port, "localhost", "http://localhost:" + port + "/health",
                attribs);

        port(port);
        get("/health", (req, res) -> {
            System.out.println("Health check called.");
            Thread.sleep(new Random().nextInt(300));
            return "";
        });
        awaitInitialization();
        final HttpResponse httpResponse = HttpRequest.post(endpoint).body(gson.toJson(reg)).send();
        final String appId = gson.fromJson(httpResponse.body(), String.class);

        System.out.println(appId);
        newSingleThreadScheduledExecutor().scheduleAtFixedRate(
                () -> {
                    System.out.println("Pinging the server");
                    HttpRequest.put(endpoint + "/" + appId + "/ping").send();
                }, 0L, 15L, TimeUnit.SECONDS
        );

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                    HttpRequest.delete(endpoint + "/" + appId).send();
                } catch (final Exception ignored) {
                }
            }
        });
    }
}
