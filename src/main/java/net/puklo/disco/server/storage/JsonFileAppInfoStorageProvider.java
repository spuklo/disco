package net.puklo.disco.server.storage;

import net.puklo.disco.model.AppInfo;
import net.puklo.disco.model.AppReg;
import net.puklo.disco.server.AppStorage;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public class JsonFileAppInfoStorageProvider implements AppStorage {

    @Override
    public UUID postAppInfo(AppReg appReg) {
        return null;
    }

    @Override
    public Optional<UUID> putAppInfo(AppInfo appInfo) {
        return null;
    }

    @Override
    public Optional<AppInfo> getAppInfo(UUID appId) {
        return null;
    }

    @Override
    public Optional<AppInfo> deleteAppInfo(UUID appId) {
        return null;
    }

    @Override
    public Collection<AppInfo> getAllStoredAppInfo() {
        return null;
    }
}