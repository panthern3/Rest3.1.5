package ru.kata.spring.boot_security.demo.example.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = {"ru.kata.spring.boot_security.demo.example.repository"})
@EntityScan(basePackages = {"ru.kata.spring.boot_security.demo.example.model"})

public class JpaConfig {
    // Другие настройки
}
