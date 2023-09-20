package com.rendi.RendiBackend.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.rendi.RendiBackend.repositories")
public class JpaConfig {
    // ...
}
