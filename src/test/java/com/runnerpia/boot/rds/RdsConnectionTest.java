package com.runnerpia.boot.rds;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class RdsConnectionTest {

    @Autowired
    private DataSource dataSource;

    @Test
    public void testJpaRdsConnection() {
        try (Connection connection = dataSource.getConnection()) {
            assertThat(connection).isNotNull();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
