package dev.msiviero.config;

import javax.inject.Inject;

public class DatabaseConfiguration {

    @Inject
    DatabaseConfiguration() {
    }

    public String databaseName() {
        return "app";
    }

    public String databaseHost() {
        return "127.0.0.1";
    }

    public String databaseUsername() {
        return "api";
    }

    public String databasePassword() {
        return "password";
    }
}
