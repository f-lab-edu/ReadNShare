package com.flab.readnshare.support;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
public abstract class IntegrationTestSupport {
    private static final String MYSQL_IMAGE = "mysql:8.0";
    private static final String REDIS_IMAGE = "redis:6-alpine";

    private static final MySQLContainer<?> mysqlContainer = new MySQLContainer<>(MYSQL_IMAGE)
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    private static final GenericContainer<?> redisContainer = new GenericContainer<>(DockerImageName.parse(REDIS_IMAGE))
            .withExposedPorts(6379);

    static {
        mysqlContainer.start();
        redisContainer.start();
    }

    @DynamicPropertySource
    public static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);

        registry.add("spring.data.redis.host", redisContainer::getHost);
        registry.add("spring.data.redis.port", redisContainer::getFirstMappedPort);
    }
}
