package net.puklo.disco.server;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static net.puklo.disco.server.DiscoConfig.DS_HEALTH_CHECK_TIMEOUT;
import static net.puklo.disco.server.DiscoConfig.DS_HTTP_PORT;
import static net.puklo.disco.server.DiscoConfig.DS_JSON_PRETTY_PRINT;
import static net.puklo.disco.server.DiscoConfig.DS_POLLING_INTERVAL;
import static net.puklo.disco.server.DiscoConfig.DS_POLLING_WORKERS;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class DiscoConfigTest {

    private DiscoConfig config;

    @Before
    public void before_each_test() {
        System.clearProperty(DS_HTTP_PORT);
        System.clearProperty(DS_POLLING_INTERVAL);
        System.clearProperty(DS_POLLING_WORKERS);
        System.clearProperty(DS_HEALTH_CHECK_TIMEOUT);
        System.clearProperty(DS_JSON_PRETTY_PRINT);
    }

    @After
    public void after_each_test() {
        before_each_test();
    }

    @Test
    public void should_return_defaults_if_no_config_options_are_set_as_JVM_args() throws Exception {
        config = new DiscoConfig();

        assertThat(config.httpPort(), is(2001));
        assertThat(config.pollingInterval(), is(60));
        assertThat(config.healthCheckTimeout(), is(5));
        assertThat(config.numberOfPollingWorkers(), is(Runtime.getRuntime().availableProcessors()));
        assertEquals(false, config.useJsonPrettyPrint());
    }

    @Test
    public void should_return_custom_http_port_if_defined() throws Exception {
        System.setProperty(DS_HTTP_PORT, "1234");

        config = new DiscoConfig();

        assertThat(config.httpPort(), is(1234));
    }

    @Test
    public void should_return_custom_polling_interval_if_set() throws Exception {
        System.setProperty(DS_POLLING_INTERVAL, "2234");

        config = new DiscoConfig();

        assertThat(config.pollingInterval(), is(2234));
    }

    @Test
    public void should_return_custom_number_of_polling_wrokers_if_set() throws Exception {
        System.setProperty(DS_POLLING_WORKERS, "2334");

        config = new DiscoConfig();

        assertThat(config.numberOfPollingWorkers(), is(2334));
    }

    @Test
    public void should_return_custom_timeout_if_set() throws Exception {
        System.setProperty(DS_HEALTH_CHECK_TIMEOUT, "2354");

        config = new DiscoConfig();

        assertThat(config.healthCheckTimeout(), is(2354));
    }

    @Test
    public void should_return_custom_json_pretty_print_option_if_set() throws Exception {
        System.setProperty(DS_JSON_PRETTY_PRINT, "true");

        config = new DiscoConfig();

        assertTrue(config.useJsonPrettyPrint());
    }

}