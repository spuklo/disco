package net.puklo.disco.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DiscoConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(DiscoConfig.class);

    public static final String DS_HTTP_PORT = "ds.http.port";
    public static final String DS_POLLING_INTERVAL = "ds.polling.interval";
    public static final String DS_POLLING_WORKERS = "ds.polling.workers";
    public static final String DS_HEALTH_CHECK_TIMEOUT = "ds.health.check.timeout";
    public static final String DS_JSON_PRETTY_PRINT = "ds.json.pretty.print";

    private final Integer httpPort;
    private final Integer pollingInterval;
    private final Integer numberOfpollingWorkers;
    private final Integer healthCheckTimeout;
    private final Boolean useJsonPrettyPrint;

    public DiscoConfig() {
        httpPort = getOptionAsInteger(DS_HTTP_PORT, 2001);
        pollingInterval = getOptionAsInteger(DS_POLLING_INTERVAL, 60);
        numberOfpollingWorkers = getOptionAsInteger(DS_POLLING_WORKERS,
                Runtime.getRuntime().availableProcessors());
        healthCheckTimeout = getOptionAsInteger(DS_HEALTH_CHECK_TIMEOUT, 5);
        useJsonPrettyPrint = Boolean.getBoolean(DS_JSON_PRETTY_PRINT);

    }

    public Integer httpPort() {
        return httpPort;
    }

    public Integer pollingInterval() {
        return pollingInterval;
    }

    public Integer numberOfPollingWorkers() {
        return numberOfpollingWorkers;
    }

    public Integer healthCheckTimeout() {
        return healthCheckTimeout;
    }

    public Boolean useJsonPrettyPrint() {
        return useJsonPrettyPrint;
    }

    private Integer getOptionAsInteger(final String option, final Integer defaultValue) {
        try {
            LOGGER.debug("Trying to get value for option {}", option);
            final Integer integer = Integer.valueOf(System.getProperty(option));
            LOGGER.debug("Option {} is set to {}", option, integer);
            return integer;
        } catch (final NumberFormatException ignored) {
            LOGGER.debug("Option {} not set, setting default value of {}", option, defaultValue);
            return defaultValue;
        }
    }
}
