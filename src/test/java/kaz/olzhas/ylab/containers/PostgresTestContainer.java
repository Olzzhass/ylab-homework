package kaz.olzhas.ylab.containers;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.PostgreSQLContainer;

public abstract class PostgresTestContainer {
    public static final String IMAGE_VERSION = "postgres:15";

    public static PostgreSQLContainer<?> container = new PostgreSQLContainer<>(IMAGE_VERSION);

    @BeforeAll
    static void start() {
        container.start();
    }

    @AfterAll
    static void stop() {
        container.stop();
    }
}
