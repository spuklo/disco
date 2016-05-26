package net.puklo.disco.server;

import com.google.gson.Gson;
import net.puklo.disco.model.AppInfo;
import net.puklo.disco.model.AppReg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.UUID;

import static javax.servlet.http.HttpServletResponse.SC_CREATED;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static spark.Spark.after;
import static spark.Spark.awaitInitialization;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.halt;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.put;

public class DiscoWebServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DiscoWebServer.class);

    private final Integer httpPort;
    private final Gson gson;
    private final AppStorage appStorageBackend;

    public DiscoWebServer(final Integer httpPort,
                          final Gson gson,
                          final AppStorage appStorageBackend) {
        this.httpPort = httpPort;
        this.gson = gson;
        this.appStorageBackend = appStorageBackend;
        initializeRoutes();
    }

    private void initializeRoutes() {
        port(httpPort);

        get("/v1/apps", (request, response) -> {
            return appStorageBackend.getAllStoredAppInfo();
        }, gson::toJson);

        get("/v1/apps/:appId", (request, response) -> {
            final UUID appId = appIdOr404(request.params("appId"));
            LOGGER.info("Retrieving app with id: {}", appId);
            return appOr404(appStorageBackend.getAppInfo(appId));
        }, gson::toJson);

        post("/v1/apps", (request, response) -> {
            final AppReg appReg = gson.fromJson(request.body(), AppReg.class);
            final UUID appId = appStorageBackend.postAppInfo(appReg);
            response.status(SC_CREATED);
            LOGGER.info("App registered: " + appReg);
            return appId.toString();
        }, gson::toJson);

        put("/v1/apps/:appId/ping", (request, response) -> {
            final UUID appId = appIdOr404(request.params("appId"));
            final AppInfo anApp = appOr404(appStorageBackend.getAppInfo(appId));
            anApp.updateLastPingTime();
            appStorageBackend.putAppInfo(anApp);
            LOGGER.info("Ping update from app with id {}", appId);
            return null;
        }, gson::toJson);

        delete("/v1/apps/:appId", (request, response) -> {
            final UUID appId = appIdOr404(request.params("appId"));
            appOr404(appStorageBackend.deleteAppInfo(appId));
            LOGGER.info("App with id: {} has been removed.", appId);
            return null;
        }, gson::toJson);

        awaitInitialization();
        after((request, response) -> response.type("application/json"));
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private AppInfo appOr404(final Optional<AppInfo> maybeApp) {
        if (maybeApp.isPresent()) {
            return maybeApp.get();
        }
        halt(SC_NOT_FOUND);
        return null;
    }

    private UUID appIdOr404(final String maybeUuid) {
        try {
            return UUID.fromString(maybeUuid);
        } catch (final IllegalArgumentException ignored) {
            halt(SC_NOT_FOUND);
            return null;
        }
    }

}
