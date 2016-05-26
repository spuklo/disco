package net.puklo.disco.server.storage;

import net.puklo.disco.model.AppInfo;
import net.puklo.disco.model.AppReg;
import net.puklo.disco.server.AppStorage;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

public class InMemoryAppStorage implements AppStorage {

    private final ConcurrentMap<UUID, AppInfo> apps = new ConcurrentHashMap<>();
    private final Supplier<UUID> appIdGenerator;

    public InMemoryAppStorage(final Supplier<UUID> appIdGenerator) {
        this.appIdGenerator = appIdGenerator;
    }

    @Override
    public UUID postAppInfo(final AppReg appReg) {
        requireNonNull(appReg, "AppReg object can't be null.");

        final UUID newAppId = appIdGenerator.get();
        apps.put(newAppId, new AppInfo(appReg, newAppId));
        return newAppId;
    }

    @Override
    public Optional<UUID> putAppInfo(final AppInfo appInfo) {
        requireNonNull(appInfo, "AppInfo object can't be null.");
        requireNonNull(appInfo.appId(), "App's Id can't be null.");

        if (!apps.containsKey(appInfo.appId())) {
            return Optional.empty();
        }

        apps.put(appInfo.appId(), appInfo);
        return Optional.of(appInfo.appId());
    }

    @Override
    public Optional<AppInfo> getAppInfo(final UUID appId) {
        requireNonNull(appId, "AppId object can't be null.");

        if (!apps.containsKey(appId)) {
            return Optional.empty();
        }
        try {
            return Optional.of(apps.get(appId).clone());
        } catch (final CloneNotSupportedException ignored) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<AppInfo> deleteAppInfo(final UUID appId) {
        requireNonNull(appId, "AppId object can't be null.");

        if (!apps.containsKey(appId)) {
            return Optional.empty();
        }
        return Optional.of(apps.remove(appId));
    }

    @Override
    public Map<UUID, AppInfo> getAllStoredAppInfo() {
        return Collections.unmodifiableMap(apps);
    }
}
