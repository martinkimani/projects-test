package com.kcb.api.projectsTest;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.MountableFile;

@TestConfiguration(proxyBeanMethods = false)
public class ContainersConfig {
    
    @SuppressWarnings("rawtypes")
    @Bean
    @ServiceConnection
    MySQLContainer mysqlContainer() {
        MySQLContainer mysql = new MySQLContainer("mysql:latest");
        mysql.withCopyFileToContainer(MountableFile.forClasspathResource("schema.sql"), "/docker-entrypoint-initdb.d/schema.sql");
        return mysql;

    }

}
