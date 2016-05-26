package net.puklo.disco.server;

import net.puklo.disco.model.AppInfo;
import net.puklo.disco.model.AppReg;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface AppStorage {
    UUID postAppInfo(AppReg appReg);
    Optional<UUID> putAppInfo(AppInfo appInfo);
    Optional<AppInfo> getAppInfo(UUID appId);
    Optional<AppInfo> deleteAppInfo(UUID appId);
    Map<UUID, AppInfo> getAllStoredAppInfo();
}
