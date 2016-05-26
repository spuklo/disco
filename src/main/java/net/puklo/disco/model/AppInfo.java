package net.puklo.disco.model;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.Objects.requireNonNull;
import static net.puklo.disco.model.AppStatus.OK;
import static net.puklo.disco.Toolbox.notEmpty;

public class AppInfo extends AppReg implements Cloneable {
    private final UUID appId;
    private final Instant registrationTime;
    private final AtomicReference<Instant> lastPollUpdate;
    private final AtomicReference<Instant> lastPingUpdate;
    private final AtomicReference<AppStatus> appStatus;

    public AppInfo(final AppReg appReg, final UUID appId) {
        super(
                notEmpty(requireNonNull(appReg).getName(), "App name can't be empty."),
                notEmpty(appReg.getVersion(), "App version can't be empty."),
                notEmpty(appReg.getHostname(), "App hostname can't be empty."),
                appReg.getHealthUrl(),
                appReg.getAppAttributes()
        );
        this.appId = appId;
        this.registrationTime = Instant.now();
        lastPollUpdate = new AtomicReference<>(Instant.now());
        lastPingUpdate = new AtomicReference<>();
        appStatus = new AtomicReference<>(OK);
    }

    private AppInfo(final AppInfo appInfo) {
        super(
                appInfo.getName(),
                appInfo.getVersion(),
                appInfo.getHostname(),
                appInfo.getHealthUrl(),
                appInfo.getAppAttributes()
        );
        this.appId = appInfo.appId();
        this.registrationTime = appInfo.registrationTime();
        this.lastPollUpdate = new AtomicReference<>(appInfo.lastPollUpdate());
        this.lastPingUpdate = new AtomicReference<>(appInfo.lastPingUpdate());
        this.appStatus = new AtomicReference<>(appInfo.appStatus());
    }

    public void setStatus(final AppStatus newStatus) {
        appStatus.set(newStatus);
        updateLastUpdateTime();
    }

    public UUID appId() {
        return appId;
    }

    public Instant registrationTime() {
        return registrationTime;
    }

    public Instant lastPollUpdate() {
        return lastPollUpdate.get();
    }

    public Instant lastPingUpdate() {
        return lastPingUpdate.get();
    }

    public AppStatus appStatus() {
        return appStatus.get();
    }

    private void updateLastUpdateTime() {
        lastPollUpdate.set(Instant.now());
    }

    public void updateLastPingTime() {
        lastPingUpdate.set(Instant.now());
    }

    @Override
    public AppInfo clone() throws CloneNotSupportedException {
        return new AppInfo(this);
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "appId=" + appId +
                ", appName=" + getName() +
                ", appVersion=" + getVersion() +
                ", hostname=" + getHostname() +
                ", registered=" + registrationTime +
                (hasHealthUrl() ? ", healthCheck=" + getHealthUrl() : "") +
                (getAppAttributes().isEmpty() ? "" : ", attributes=" + getAppAttributes().toString()) +
                ", lastPollUpdate=" + lastPollUpdate +
                ", lastPingUpdate=" + lastPingUpdate +
                ", appStatus=" + appStatus +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppInfo appInfo = (AppInfo) o;
        return Objects.equals(appId, appInfo.appId) &&
                Objects.equals(registrationTime, appInfo.registrationTime) &&
                Objects.equals(lastPollUpdate, appInfo.lastPollUpdate) &&
                Objects.equals(lastPingUpdate, appInfo.lastPingUpdate) &&
                Objects.equals(appStatus, appInfo.appStatus) &&
                Objects.equals(getName(), appInfo.getName()) &&
                Objects.equals(getVersion(), appInfo.getVersion()) &&
                Objects.equals(getHostname(), appInfo.getHostname()) &&
                Objects.equals(getHealthUrl(), appInfo.getHealthUrl()) &&
                Objects.equals(getAppAttributes(), appInfo.getAppAttributes());
    }

    @Override
    public int hashCode() {
        return Objects.hash(appId, registrationTime, lastPollUpdate, lastPingUpdate, appStatus,
                getName(), getVersion(), getHostname(), getHealthUrl(), getAppAttributes());
    }
}
