package kr.hhplus.be.server.config;

import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

@Configuration
public class TestContainerConfig {
    public static final MySQLContainer<?> MYSQL_CONTAINER;

    static {
        MYSQL_CONTAINER = new MySQLContainer<>(DockerImageName.parse("mysql:8.0"))
                .withDatabaseName("hhplus_test")
                .withUsername("test")
                .withPassword("test");
        MYSQL_CONTAINER.start();

        System.setProperty("spring.datasource.url", MYSQL_CONTAINER.getJdbcUrl() + "?characterEncoding=UTF-8&serverTimezone=UTC");
        System.setProperty("spring.datasource.username", MYSQL_CONTAINER.getUsername());
        System.setProperty("spring.datasource.password", MYSQL_CONTAINER.getPassword());
    }

    @PreDestroy
    public void preDestroy() {
        if (MYSQL_CONTAINER.isRunning()) {
            MYSQL_CONTAINER.stop();
        }
    }

    private static final String REDIS_IMAGE = "redis:latest";
    private static final int REDIS_PORT = 6379;
    private static final GenericContainer REDIS_CONTAINER;

    static {
        REDIS_CONTAINER = new GenericContainer(REDIS_IMAGE)
                .withExposedPorts(REDIS_PORT)
                .withReuse(true);
        REDIS_CONTAINER.start();
    }

    private static final String KAFKA_IMAGE = "confluentinc/cp-kafka:latest";
    private static final int KAFKA_PORT = 9092;
    private static final GenericContainer<?> KAFKA_CONTAINER;

    static {
        KAFKA_CONTAINER = new GenericContainer<>(DockerImageName.parse(KAFKA_IMAGE))
                .withExposedPorts(KAFKA_PORT)
                .withEnv("KAFKA_ADVERTISED_LISTENERS", "PLAINTEXT://localhost:" + KAFKA_PORT)
                .withEnv("KAFKA_ZOOKEEPER_CONNECT", "localhost:2181")
                .withReuse(true);
        KAFKA_CONTAINER.start();
    }

}
