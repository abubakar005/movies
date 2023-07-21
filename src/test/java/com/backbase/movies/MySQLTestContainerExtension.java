package com.backbase.movies;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.MySQLContainer;

public class MySQLTestContainerExtension implements BeforeAllCallback, BeforeEachCallback, ExtensionContext.Store.CloseableResource {

    private static final MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:latest")
            .withDatabaseName("db_movies")
            .withUsername("test")
            .withPassword("test")
            .withReuse(true);

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        if (!mysqlContainer.isRunning()) {
            mysqlContainer.start();
        }
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        context.getRoot().getStore(ExtensionContext.Namespace.GLOBAL).getOrComputeIfAbsent("container", k -> this);
    }

    @Override
    public void close() {
        if (mysqlContainer != null && mysqlContainer.isRunning()) {
            mysqlContainer.stop();
        }
    }
}

