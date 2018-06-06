package com.ibm.cdays.configuration;

import com.ibm.cdays.Application;
import com.ibm.cdays.common.config.Configs;
import com.ibm.cdays.common.config.annotations.ConfigurationProperties;
import com.ibm.cdays.common.config.annotations.Optional;
import com.ibm.cdays.common.config.annotations.Value;

/**
 * @author Michael Wellner (michael.wellner@mobi.ch).
 */
@ConfigurationProperties
public class ApplicationConfiguration {

    public static final ApplicationConfiguration cdays = Configs.mapToConfigClass(ApplicationConfiguration.class, "cdays");

    @Value("api")
    public final API api;

    @Value("build")
    public final String build;

    @Value("name")
    public final String name;

    @SuppressWarnings("unused")
    private ApplicationConfiguration() {
        this(null, null, null);
    }

    public ApplicationConfiguration(API api, String build, String name) {
        this.api = api;
        this.build = build;
        this.name = name;
    }

    @ConfigurationProperties
    public static class API {

        @Value("hostname")
        public final String hostname;

        @Value("port")
        public final int port;

        @SuppressWarnings("unused")
        private API() {
            this("localhost", 8080);
        }

        public API(String hostname, int port) {
            this.hostname = hostname;
            this.port = port;
        }

    }

}
