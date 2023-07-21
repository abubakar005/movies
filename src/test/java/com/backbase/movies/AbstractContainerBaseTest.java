package com.backbase.movies;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;

public abstract class AbstractContainerBaseTest {

    protected static MySQLContainer mySQLContainer = (MySQLContainer) new MySQLContainer("mysql:latest")
            .withDatabaseName("db_movies")
            .withUsername("test")
            .withPassword("test")
            .withReuse(true);

    @DynamicPropertySource
    static void mySQLTestDbProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
    }
}
