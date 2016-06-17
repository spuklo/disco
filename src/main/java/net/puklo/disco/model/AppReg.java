package net.puklo.disco.model;

import java.util.Map;

import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;

public class AppReg {
    private final String name;
    private final String version;
    private final String hostname;
    private final String healthUrl;
    private final Map<String, String> appAttributes;

    public AppReg(final String name,
                  final String version,
                  final String hostname,
                  final String healthUrl,
                  final Map<String, String> appAttributes) {
        this.name = name;
        this.version = version;
        this.hostname = hostname;
        this.healthUrl = healthUrl;
        this.appAttributes = unmodifiableMap(appAttributes);
    }

    public AppReg(final String name,
                  final String version,
                  final String hostname,
                  final String healthUrl) {
        this(name, version, hostname, healthUrl, emptyMap());
    }

    public AppReg(final String name,
                  final String version,
                  final String hostname) {
        this(name, version, hostname, null, emptyMap());
    }

    public AppReg(final String name,
                  final String version,
                  final String hostname,
                  final Map<String, String> appAttributes) {
        this(name, version, hostname, null, appAttributes);
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getHostname() {
        return hostname;
    }

    public String getHealthUrl() {
        return healthUrl;
    }

    public Boolean hasHealthUrl() {
        return healthUrl != null && !healthUrl.isEmpty();
    }

    public Map<String, String> getAppAttributes() {
        return appAttributes;
    }

    private Map verifyValidKeys(final Map<String, String> inputMap) {
        if (inputMap.isEmpty()) {
            return inputMap;
        }
        if (inputMap.keySet().contains("name") || inputMap.keySet().contains("hostname")) {
            throw new IllegalArgumentException("Attributes with names 'name' and 'hostname' are not allowed.");
        }
        return inputMap;
    }

    @Override
    public String toString() {
        return "AppReg{" +
                "name='" + name + '\'' +
                ", version='" + version + '\'' +
                ", hostname='" + hostname + '\'' +
                ", healthUrl=" + healthUrl +
                ", appAttributes=" + appAttributes +
                '}';
    }
}
