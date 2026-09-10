package com.example.stadiumtickets.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DbSchemaMigration implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DbSchemaMigration.class);

    private final JdbcTemplate jdbcTemplate;

    public DbSchemaMigration(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) {
        try {
            jdbcTemplate.execute("ALTER TABLE orders ALTER COLUMN employee_id DROP NOT NULL");
            log.info(">>> Миграция БД: orders.employee_id теперь допускает NULL (онлайн-покупки без продавца)");
        } catch (Exception e) {
            log.warn(">>> Миграция orders.employee_id не выполнена: {}", e.getMessage());
        }
    }
}
