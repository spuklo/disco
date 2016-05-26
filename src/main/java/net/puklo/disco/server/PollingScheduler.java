package net.puklo.disco.server;

import jodd.http.HttpException;
import jodd.http.HttpRequest;
import jodd.http.HttpResponse;
import net.puklo.disco.model.AppInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static javax.servlet.http.HttpServletResponse.SC_OK;
import static net.puklo.disco.model.AppStatus.OK;
import static net.puklo.disco.model.AppStatus.UNAVAILABLE;

public class PollingScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(PollingScheduler.class);

    private final ExecutorService healthCheckExecutorsPool;
    private final Integer healthCheckTimeout;
    private final AtomicInteger healthCheckWorkers = new AtomicInteger(0);

    public PollingScheduler(final Integer pollingInterval,
                            final Integer healthCheckersThreadPool,
                            final Integer healthCheckTimeout,
                            final Supplier<Map<UUID, AppInfo>> allAppsGetter) {

        final ScheduledExecutorService pollingScheduler = Executors.newSingleThreadScheduledExecutor(
                runnable -> new Thread(runnable, "healthCheckScheduler"));

        healthCheckExecutorsPool = Executors.newFixedThreadPool(healthCheckersThreadPool,
                runnable -> new Thread(runnable, "healthCheckWorker-" + healthCheckWorkers.incrementAndGet()));

        this.healthCheckTimeout = healthCheckTimeout;

        pollingScheduler.scheduleAtFixedRate(mainScheduler(allAppsGetter), 1L, pollingInterval, TimeUnit.SECONDS);
    }

    private Runnable mainScheduler(final Supplier<Map<UUID, AppInfo>> allAppsGetter) {
        return () -> {
            final Stream<AppInfo> appsWithHealthCheckUrl = allAppsGetter.get().values().stream().filter(app -> app.hasHealthUrl());
            appsWithHealthCheckUrl.forEach(app ->
                    healthCheckExecutorsPool.submit(() -> {
                        try {
                            LOGGER.info(String.format("Checking the %s (%s)", app.getName(), app.getHealthUrl()));
                            LOGGER.debug("Checking application: {}", app);
                            final HttpResponse response = HttpRequest
                                    .get(app.getHealthUrl())
                                    .timeout(healthCheckTimeout * 1000)
                                    .send();
                            if (response.statusCode() == SC_OK) {
                                app.setStatus(OK);
                                LOGGER.info(String.format("App %s is OK", app.getName()));
                            } else {
                                app.setStatus(UNAVAILABLE);
                                LOGGER.warn(String.format("App %s %s running on %s (health check URL: %s) has responded with non-OK HTTP code %d",
                                        app.getName(), app.getVersion(), app.getHostname(), app.getHealthUrl(), response.statusCode()));
                            }
                        } catch (final HttpException ignored) {
                            app.setStatus(UNAVAILABLE);
                            LOGGER.warn(String.format("Failed to connect to health check URL %s for the %s app (version %s)",
                                    app.getHealthUrl(), app.getName(), app.getVersion()));
                        }
                        return null;
                    })
            );
        };
    }

}
