package net.puklo.disco.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.puklo.disco.server.storage.InMemoryAppStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class DiscoServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DiscoServer.class);

    public DiscoServer(final DiscoConfig config) {
        LOGGER.info("Initializing disco server. Put the music on!");
        final AppStorage appStorage = new InMemoryAppStorage(UUID::randomUUID);

        new DiscoWebServer(config.httpPort(), buildGsonInstance(config.useJsonPrettyPrint()), appStorage);

        new PollingScheduler(
                config.pollingInterval(),
                config.numberOfPollingWorkers(),
                config.healthCheckTimeout(),
                appStorage::getAllStoredAppInfo);
        LOGGER.info("Disco server has started! Turn the music up!");
    }

    public static void main(final String[] args) {
        new DiscoServer(new DiscoConfig());
    }

    private Gson buildGsonInstance(final Boolean shouldUsePrettyPrinting) {
        return shouldUsePrettyPrinting
                ? new GsonBuilder().setPrettyPrinting().create()
                : new Gson();
    }

}
