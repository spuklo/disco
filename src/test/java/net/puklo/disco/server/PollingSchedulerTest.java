package net.puklo.disco.server;

import net.puklo.disco.model.AppInfo;
import net.puklo.disco.model.AppReg;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PollingSchedulerTest {

    private static final Integer POLLING_INTERVAL_IN_SECONDS = 1;
    private static final Integer HTTP_HEALTH_CHECK_THREAD_POOL = 1;
    private static final Integer HTTP_HEALTH_CHECK_TIMEOUT = 1;

    @Mock
    private Supplier<Map<UUID, AppInfo>> allAppsGetter;

    @Test
    public void should_poll_at_least_once_in_double_of_the_defined_interval() throws Exception {

        final PollingScheduler scheduler = getSchedulerInstance();

        // sleep double the polling period
        Thread.sleep(2 * POLLING_INTERVAL_IN_SECONDS * 1000L);

        verify(allAppsGetter, atLeastOnce()).get();
        verify(allAppsGetter, atMost(3 * POLLING_INTERVAL_IN_SECONDS)).get();
    }

    @Test
    public void should_poll_only_apps_with_health_check_url() throws Exception {
        when(allAppsGetter.get()).thenReturn(testApps());

        final PollingScheduler scheduler = getSchedulerInstance();

        // sleep double the polling period
        Thread.sleep(2 * POLLING_INTERVAL_IN_SECONDS * 1000L);

    }

    private PollingScheduler getSchedulerInstance() {
        return new PollingScheduler(POLLING_INTERVAL_IN_SECONDS,
                HTTP_HEALTH_CHECK_THREAD_POOL, HTTP_HEALTH_CHECK_TIMEOUT, allAppsGetter);
    }

    private Map<UUID, AppInfo> testApps() {
        final UUID appId1 = UUID.fromString("9ffd8b46-d4d4-4a7d-98dc-bb903bdf3024");
        final UUID appId2 = UUID.fromString("031e8657-9066-40ce-9c0d-aba6c1820474");

        final Map<UUID, AppInfo> daMap = new HashMap<>();
        daMap.put(appId1, new AppInfo(new AppReg("app1", "1.0.0", "foohost"), appId1));
        daMap.put(appId2, new AppInfo(new AppReg("app2", "2.0.0", "barhost", "http://localhost:12345/foobar"), appId2));
        return daMap;
    }
}